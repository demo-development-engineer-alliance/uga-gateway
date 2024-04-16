package com.pengzexuan.uga.core.netty.processor.impl;

import com.pengzexuan.uga.core.context.UgaHttpRequestContext;
import com.pengzexuan.uga.core.netty.processor.UgaNettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UgaNettyCoreProcessor implements UgaNettyProcessor {


	private static final Logger log = LoggerFactory.getLogger(UgaNettyCoreProcessor.class);

	@Override
	public void process(@NotNull UgaHttpRequestContext context) {
		FullHttpRequest fullHttpRequest = context.getRequest();
		ChannelHandlerContext channelHandlerContext = context.getCtx();
		log.info("Uga Netty Core processing request: {}", fullHttpRequest);
	}


	@Override
	public void start() {

	}


	@Override
	public void shutdown() {

	}
}
