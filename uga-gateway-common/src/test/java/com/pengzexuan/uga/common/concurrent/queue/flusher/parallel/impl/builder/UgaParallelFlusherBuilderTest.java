package com.pengzexuan.uga.common.concurrent.queue.flusher.parallel.impl.builder;

import com.pengzexuan.uga.common.concurrent.queue.flusher.parallel.impl.UgaParallelFlusher;
import org.junit.Assert;
import org.junit.Test;

public class UgaParallelFlusherBuilderTest {

	@Test
	public void testSetProducerType() {
		UgaParallelFlusher.Builder<Object> parallelFlusherBuilder = new UgaParallelFlusher.Builder<>();
		try {
			parallelFlusherBuilder.setProducerType(null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testSetBufferSize() {
		UgaParallelFlusher.Builder<Object> parallelFlusherBuilder = new UgaParallelFlusher.Builder<>();
		try {
			parallelFlusherBuilder.setBufferSize(888);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}

	}

	@Test
	public void testSetThreadsNum() {
		UgaParallelFlusher.Builder<Object> parallelFlusherBuilder = new UgaParallelFlusher.Builder<>();
		try {
			parallelFlusherBuilder.setThreadsNum(-1);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testSetNamePrefix() {
		UgaParallelFlusher.Builder<Object> parallelFlusherBuilder = new UgaParallelFlusher.Builder<>();
		try {
			parallelFlusherBuilder.setNamePrefix("");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}
}