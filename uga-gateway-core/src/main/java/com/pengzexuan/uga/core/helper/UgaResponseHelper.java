package com.pengzexuan.uga.core.helper;


import com.pengzexuan.uga.common.enums.UgaResponseCode;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.jetbrains.annotations.NotNull;


public class UgaResponseHelper {


	@NotNull
	public static FullHttpResponse getHttpResponse(UgaResponseCode responseCode) {
		String errorContent = "Response internal error";
		DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
				HttpResponseStatus.INTERNAL_SERVER_ERROR,
				Unpooled.wrappedBuffer(errorContent.getBytes()));
		
		httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
		httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, errorContent.length());
		return httpResponse;
	}
	
	
	
}
