package com.pengzexuan.uga.core.helper;

import org.asynchttpclient.*;

import java.util.concurrent.CompletableFuture;

public class UgaAsyncHttpHelper {

	private static final class SingletonHolder {
		private static final UgaAsyncHttpHelper INSTANCE = new UgaAsyncHttpHelper();
	}
	
	private UgaAsyncHttpHelper() {
		
	}
	
	public static UgaAsyncHttpHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private AsyncHttpClient asyncHttpClient;
	
	public void initialized(AsyncHttpClient asyncHttpClient) {
		this.asyncHttpClient = asyncHttpClient;
	}
	
	public CompletableFuture<Response> executeRequest(Request request) {
		ListenableFuture<Response> future = asyncHttpClient.executeRequest(request);
		return future.toCompletableFuture();
	}
	
	public <T> CompletableFuture<T> executeRequest(Request request, AsyncHandler<T> handler) {
		ListenableFuture<T> future = asyncHttpClient.executeRequest(request, handler);
		return future.toCompletableFuture();
	}
	
}
