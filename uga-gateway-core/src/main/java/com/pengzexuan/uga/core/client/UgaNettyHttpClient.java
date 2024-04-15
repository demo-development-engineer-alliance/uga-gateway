package com.pengzexuan.uga.core.client;

import com.pengzexuan.uga.core.config.UgaConfiguration;
import com.pengzexuan.uga.core.helper.AsyncHttpHelper;
import com.pengzexuan.uga.core.lifecycle.LifeCycle;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;


@Slf4j
public class UgaNettyHttpClient implements LifeCycle {

	private final UgaConfiguration configuration;

	private final EventLoopGroup workerEventLoopGroup;

	private AsyncHttpClient asyncHttpClient;

	private DefaultAsyncHttpClientConfig.Builder clientBuilder;


	public UgaNettyHttpClient(UgaConfiguration configuration, EventLoopGroup workerEventLoopGroup) {
		this.configuration = configuration;
		this.workerEventLoopGroup = workerEventLoopGroup;
		this.init();
	}

	/**
   * Init
   */
  @Override
  public void init() {
	  this.clientBuilder = new DefaultAsyncHttpClientConfig.Builder()
			  .setFollowRedirect(false)
			  .setEventLoopGroup(this.workerEventLoopGroup)
			  .setConnectTimeout(this.configuration.getHttpConnectTimeout())
			  .setRequestTimeout(this.configuration.getHttpRequestTimeout())
			  .setMaxRequestRetry(this.configuration.getHttpMaxRequestRetry())
			  .setAllocator(PooledByteBufAllocator.DEFAULT)
			  .setCompressionEnforced(true)
			  .setMaxConnections(this.configuration.getHttpMaxConnections())
			  .setMaxConnectionsPerHost(this.configuration.getHttpConnectionsPerHost())
			  .setPooledConnectionIdleTimeout(this.configuration.getHttpPooledConnectionIdleTimeout());
  }

  /**
   * Start
   */
  @Override
  public void start() {
	  this.asyncHttpClient = new DefaultAsyncHttpClient(clientBuilder.build());
	  AsyncHttpHelper.getInstance().initialized(asyncHttpClient);
  }

  /**
   * ShutDown
   */
  @Override
  public void shutdown() {
	  if(asyncHttpClient != null) {
		  try {
			  this.asyncHttpClient.close();
		  } catch (IOException e) {
			  // ignore
			  log.error("#NettyHttpClient.shutdown# shutdown error", e);
		  }
	  }
  }
}
