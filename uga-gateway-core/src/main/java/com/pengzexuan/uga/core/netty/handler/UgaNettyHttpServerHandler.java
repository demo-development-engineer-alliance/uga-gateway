package com.pengzexuan.uga.core.netty.handler;

import com.pengzexuan.uga.core.context.UgaHttpRequestContext;
import com.pengzexuan.uga.core.netty.processor.UgaNettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UgaNettyHttpServerHandler extends ChannelInboundHandlerAdapter {

	private final UgaNettyProcessor nettyProcessor;

	public UgaNettyHttpServerHandler(UgaNettyProcessor nettyProcessor) {
		this.nettyProcessor = nettyProcessor;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {

			FullHttpRequest request = (FullHttpRequest) msg;

			UgaHttpRequestContext requestContext = new UgaHttpRequestContext();
			requestContext.setRequest(request);
			requestContext.setCtx(ctx);

			nettyProcessor.process(requestContext);

		} else {
			//	never go this way, ignore
			log.error("#NettyHttpServerHandler.channelRead# message type is not httpRequest: {}", msg);
			boolean release = ReferenceCountUtil.release(msg);
			if(!release) {
				log.error("#NettyHttpServerHandler.channelRead# release fail 资源释放失败");
			}
		}
	}
}
