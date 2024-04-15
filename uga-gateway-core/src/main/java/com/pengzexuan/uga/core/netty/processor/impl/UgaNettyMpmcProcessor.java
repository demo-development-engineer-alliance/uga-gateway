package com.pengzexuan.uga.core.netty.processor.impl;

import com.pengzexuan.uga.core.config.UgaConfiguration;
import com.pengzexuan.uga.core.context.UgaHttpRequestContext;
import com.pengzexuan.uga.core.netty.processor.UgaNettyProcessor;

public class UgaNettyMpmcProcessor implements UgaNettyProcessor {

	private final UgaConfiguration configuration;

	private final UgaNettyCoreProcessor nettyCoreProcessor;


	public UgaNettyMpmcProcessor(UgaConfiguration configuration, UgaNettyCoreProcessor nettyCoreProcessor) {
		this.configuration = configuration;
		this.nettyCoreProcessor = nettyCoreProcessor;
	}

	/**
	 * @param context
	 */
	@Override
	public void process(UgaHttpRequestContext context) {

	}

	/**
	 *
	 */
	@Override
	public void start() {

	}

	/**
	 *
	 */
	@Override
	public void shutdown() {

	}
}
