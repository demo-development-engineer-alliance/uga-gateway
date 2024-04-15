package com.pengzexuan.uga.core.netty.processor;

import com.pengzexuan.uga.core.context.UgaHttpRequestContext;

public interface UgaNettyProcessor {


	void process(UgaHttpRequestContext context);


	void start();


	void shutdown();
}
