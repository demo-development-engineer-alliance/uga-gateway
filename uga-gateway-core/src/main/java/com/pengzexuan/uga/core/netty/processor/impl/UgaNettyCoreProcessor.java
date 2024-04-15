package com.pengzexuan.uga.core.netty.processor.impl;

import com.pengzexuan.uga.core.context.UgaHttpRequestContext;
import com.pengzexuan.uga.core.netty.processor.UgaNettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.jetbrains.annotations.NotNull;

public class UgaNettyCoreProcessor implements UgaNettyProcessor {


	@Override
	public void process(@NotNull UgaHttpRequestContext context) {
		FullHttpRequest fullHttpRequest = context.getRequest();
		ChannelHandlerContext channelHandlerContext = context.getCtx();
		throw new RuntimeException("Not implemented");
	}


	@Override
	public void start() {

	}


	@Override
	public void shutdown() {

	}
}
