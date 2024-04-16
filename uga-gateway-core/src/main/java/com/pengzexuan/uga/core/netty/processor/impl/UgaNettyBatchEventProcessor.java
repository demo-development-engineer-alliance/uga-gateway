package com.pengzexuan.uga.core.netty.processor.impl;

import com.lmax.disruptor.dsl.ProducerType;
import com.pengzexuan.uga.common.concurrent.queue.flusher.parallel.impl.UgaParallelFlusher;
import com.pengzexuan.uga.common.enums.UgaResponseCode;
import com.pengzexuan.uga.core.config.UgaConfiguration;
import com.pengzexuan.uga.core.context.UgaHttpRequestContext;
import com.pengzexuan.uga.core.helper.UgaResponseHelper;
import com.pengzexuan.uga.core.netty.processor.UgaNettyProcessor;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class UgaNettyBatchEventProcessor implements UgaNettyProcessor {

	private static final Logger log = LoggerFactory.getLogger(UgaNettyBatchEventProcessor.class);
	private final UgaConfiguration configuration;

	private final UgaNettyCoreProcessor nettyCoreProcessor;

	private final UgaParallelFlusher<UgaHttpRequestContext> parallelFlusher;

	private static final String THREAD_NAME_PREFIX = "UgaNettyBatchEventProcessor-";


	public UgaNettyBatchEventProcessor(UgaConfiguration configuration, UgaNettyCoreProcessor nettyCoreProcessor) {
		this.configuration = configuration;
		this.nettyCoreProcessor = nettyCoreProcessor;

		UgaBatchEventProcessorListener listener = new UgaBatchEventProcessorListener();

		this.parallelFlusher = new UgaParallelFlusher.Builder<UgaHttpRequestContext>()
				.setBufferSize(this.configuration.getBufferSize())
				.setThreadsNum(this.configuration.getProcessThreadNum())
				.setProducerType(ProducerType.MULTI)
				.setNamePrefix(THREAD_NAME_PREFIX)
				.setWaitStrategy(this.configuration.getTureWaitStrategyObject())
				.setEventListener(listener)
				.build();
	}

	/**
	 * Process
	 * todo: fix this doc...
	 * @param context Context
	 */
	@Override
	public void process(UgaHttpRequestContext context) {
		log.info("#BatchEventProcessor# process request: {}", context);
		this.parallelFlusher.add(context);
	}

	public class UgaBatchEventProcessorListener implements UgaParallelFlusher.UgaParallelEventListener<UgaHttpRequestContext> {

		/**
		 * On Event
		 *
		 * @param event event
		 */
		@Override
		public void onEvent(UgaHttpRequestContext event) {
			nettyCoreProcessor.process(event);
		}

		/**
		 * On Exception
		 *
		 * @param ex ex
		 * @param sequence sequence
		 * @param event Event
		 */
		@Override
		public void onException(Throwable ex, long sequence, @NotNull UgaHttpRequestContext event) {
			HttpRequest request = event.getRequest();
			ChannelHandlerContext ctx = event.getCtx();
			try {
				log.error("#BatchEventProcessorListener# onException Request processing failed, request: {}. errorMessage: {}",
						request, ex.getMessage(), ex);

				FullHttpResponse fullHttpResponse = UgaResponseHelper.getHttpResponse(UgaResponseCode.INTERNAL_ERROR);
				if(!HttpUtil.isKeepAlive(request)) {
					ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
				} else {
					fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
					ctx.writeAndFlush(fullHttpResponse);
				}

			} catch (Exception e) {
				log.error("#BatchEventProcessorListener# onException Request for write back failed, request: {}. errorMessage: {}",
						request, e.getMessage(), e);
			}
		}
	}

	/**
	 * Start up all
	 */
	@Override
	public void start() {
		this.nettyCoreProcessor.start();
		this.parallelFlusher.start();
	}

	/**
	 *
	 */
	@Override
	public void shutdown() {
		this.nettyCoreProcessor.shutdown();
		this.parallelFlusher.shutdown();
	}
}
