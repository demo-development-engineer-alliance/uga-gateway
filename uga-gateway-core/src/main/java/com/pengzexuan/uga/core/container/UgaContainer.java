package com.pengzexuan.uga.core.container;

import com.google.gson.Gson;
import com.pengzexuan.uga.common.constants.UgaBufferHelper;
import com.pengzexuan.uga.core.client.UgaNettyHttpClient;
import com.pengzexuan.uga.core.config.UgaConfiguration;
import com.pengzexuan.uga.core.lifecycle.LifeCycle;
import com.pengzexuan.uga.core.netty.processor.UgaNettyProcessor;
import com.pengzexuan.uga.core.netty.processor.impl.UgaNettyBatchEventProcessor;
import com.pengzexuan.uga.core.netty.processor.impl.UgaNettyCoreProcessor;
import com.pengzexuan.uga.core.netty.processor.impl.UgaNettyMpmcProcessor;
import com.pengzexuan.uga.core.sever.UgaNettyHttpServer;
import lombok.extern.slf4j.Slf4j;


@SuppressWarnings({"unused"})
@Slf4j
public class UgaContainer implements LifeCycle {

    private final UgaConfiguration configuration;

    private UgaNettyProcessor nettyProcessor;

    private UgaNettyHttpServer nettyHttpServer;

    private UgaNettyHttpClient nettyHttpClient;

	public UgaContainer(UgaConfiguration configuration) {
		this.configuration = configuration;
        this.init();
	}

	/**
     * Init
     */
    @Override
    public void init() {

        UgaNettyCoreProcessor nettyCoreProcessor = new UgaNettyCoreProcessor();

        String bufferType = configuration.getBufferType();

        if(UgaBufferHelper.isFlusher(bufferType)) {
            nettyProcessor = new UgaNettyBatchEventProcessor(this.configuration, nettyCoreProcessor);
            log.debug("Uga buffer flusher is batch");
        }
        else if(UgaBufferHelper.isMpmc(bufferType)) {
            nettyProcessor = new UgaNettyMpmcProcessor(this.configuration, nettyCoreProcessor);
            log.debug("Uga buffer flusher is mpmc");
        }
        else {
            this.nettyProcessor = nettyCoreProcessor;
            log.debug("Uga buffer flusher is null");
        }

        nettyHttpServer = new UgaNettyHttpServer(this.configuration, this.nettyProcessor);

        nettyHttpClient = new UgaNettyHttpClient(this.configuration, nettyHttpServer.getWorkerEventLoopGroup());
    }

    /**
     * Start
     */
    @Override
    public void start() {
        this.nettyProcessor.start();
        this.nettyHttpServer.start();
        this.nettyHttpClient.start();
        log.info("Uga container started");
        log.info("Uga is started, ugaId is {}, server port is {}", configuration.getUgaId(), configuration.getPort());
        log.debug("configuration: {}", configuration);
    }

    /**
     * ShutDown
     */
    @Override
    public void shutdown() {
        nettyProcessor.shutdown();
        nettyHttpServer.shutdown();
        nettyHttpClient.shutdown();
        log.info("Uga container shut down");
    }
}
