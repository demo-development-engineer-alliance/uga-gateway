package com.pengzexuan.uga.core.sever;


import com.pengzexuan.uga.common.enums.UgaResponseCode;
import com.pengzexuan.uga.common.exceptions.UgaBaseException;
import com.pengzexuan.uga.core.config.UgaConfiguration;
import com.pengzexuan.uga.core.lifecycle.LifeCycle;
import com.pengzexuan.uga.core.netty.handler.UgaNettyHttpServerHandler;
import com.pengzexuan.uga.core.netty.manager.UgaNettyServerConnectionManager;
import com.pengzexuan.uga.core.netty.processor.UgaNettyProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@Data
@SuppressWarnings({"unused"})
public class UgaNettyHttpServer implements LifeCycle {

	/**
	 * Configuration
	 */
	private final UgaConfiguration configuration;

	/**
	 * Netty Processor
	 */
	private final UgaNettyProcessor nettyProcessor;

	private Integer serverPort = 8888;

	private ServerBootstrap serverBootstrap;

	private EventLoopGroup bossEventLoopGroup;

	private EventLoopGroup workerEventLoopGroup;



	public UgaNettyHttpServer(UgaConfiguration configuration, UgaNettyProcessor nettyProcessor) {
		if (configuration == null) {
			throw new UgaBaseException("configuration can't be null", UgaResponseCode.INTERNAL_ERROR);
		}

		if (nettyProcessor == null) {
			throw new UgaBaseException("nettyProcessor can't be null", UgaResponseCode.INTERNAL_ERROR);
		}

		this.configuration = configuration;
		this.nettyProcessor = nettyProcessor;

		if (configuration.getPort() != null) {
			this.serverPort = configuration.getPort();
		} else {
			log.warn("the configuration is null, default port is 8888");
		}

		isUseEpoll();
		this.init();
	}

	/**
	 * Init
	 */
	@Override
	public void init() {
		this.serverBootstrap = new ServerBootstrap();
		if (this.isUseEpoll()) {
			this.bossEventLoopGroup = new EpollEventLoopGroup(
					this.configuration.getEventLoopGroupBossNum(),
					new DefaultThreadFactory("NettyBossEpoll")
			);
			this.workerEventLoopGroup = new EpollEventLoopGroup(
					this.configuration.getEventLoopGroupWorkNum(),
					new DefaultThreadFactory("NettyWorkerEpoll")
			);
		} else {
			this.bossEventLoopGroup = new NioEventLoopGroup(
					this.configuration.getEventLoopGroupBossNum(),
					new DefaultThreadFactory("NettyBossNio")
			);
			this.workerEventLoopGroup = new NioEventLoopGroup(
					this.configuration.getEventLoopGroupWorkNum(),
					new DefaultThreadFactory("NettyWorkerNio")
			);
		}
	}

	private Boolean isUseEpoll() {
		if (configuration.getUseEPoll()) {
			Epoll.ensureAvailability();
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Start
	 */
	@Override
	public void start() {
		this.serverBootstrap
				.group(this.bossEventLoopGroup, this.workerEventLoopGroup)
				.channel(isUseEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				// sync + accept
				.option(ChannelOption.SO_BACKLOG, 1024)
				// Port rebinding is not allowed
				.option(ChannelOption.SO_REUSEADDR, Boolean.FALSE)
				// Reject Keep lived detection
				.childOption(ChannelOption.SO_KEEPALIVE, Boolean.FALSE)
				// Disable Nagle algorithm
				.childOption(ChannelOption.TCP_NODELAY, true)
				// Send Buffer Size
				.childOption(ChannelOption.SO_SNDBUF, 65535)
				// Receive Buffer Size
				.childOption(ChannelOption.SO_RCVBUF, 65535)
				// bind Port
				.localAddress(new InetSocketAddress(this.serverPort))
				// Bind Channel Handler
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) {
						// Pipeline Responsibility Chain Allocation
						ch.pipeline()
								// Http Encoding And Decoding
								.addLast(new HttpServerCodec())
                                // Http Maximum message size limit
								.addLast(new HttpObjectAggregator(configuration.getMaxContentLength()))
                                // Http Retry Mechanism
								.addLast(new HttpServerExpectContinueHandler())
                                // Uga Connection Manager
								.addLast(new UgaNettyServerConnectionManager())
                                // Uga Http Server Handler
								.addLast(new UgaNettyHttpServerHandler(nettyProcessor));
					}
				});

		if (this.configuration.getNettyAllocator()) {
			this.serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		}

		try {
			this.serverBootstrap.bind().sync();
			log.info("Uga server start up on port: {}", this.serverPort);
		} catch (Exception e) {
			throw new RuntimeException("this.serverBootstrap.bind().sync() fail!", e);
		}
	}

	/**
	 * ShutDown
	 */
	@Override
	public void shutdown() {

		if (this.workerEventLoopGroup != null) {
			this.workerEventLoopGroup.shutdownGracefully();
		}

		if (this.bossEventLoopGroup != null) {
			this.bossEventLoopGroup.shutdownGracefully();
		}
	}

}
