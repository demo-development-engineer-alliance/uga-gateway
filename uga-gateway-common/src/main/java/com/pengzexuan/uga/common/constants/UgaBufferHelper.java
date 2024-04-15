package com.pengzexuan.uga.common.constants;

import org.jetbrains.annotations.Contract;

/**
 * 	UgaBufferHelper
 * <p>网关缓冲区辅助类</p>
 *
 * @author pengzeuan
 * @since 1.0
 */
@SuppressWarnings({"unused"})
public class UgaBufferHelper {

	@Contract(value = " -> fail", pure = true)
	private UgaBufferHelper() {
		throw new IllegalStateException("Utility class");
	}

	public static final String FLUSHER = "FLUSHER";

	public static final String MPMC = "MPMC";

	@Contract(value = "null -> false", pure = true)
	public static boolean isMpmc(String bufferType) {
		return MPMC.equals(bufferType);
	}

	@Contract(value = "null -> false", pure = true)
	public static boolean isFlusher(String bufferType) {
		return FLUSHER.equals(bufferType);
	}
	
}
