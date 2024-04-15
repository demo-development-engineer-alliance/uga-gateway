package com.pengzexuan.uga.common.concurrent.queue.flusher.parallel.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.pengzexuan.uga.common.concurrent.queue.flusher.UgaFlusher;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class UgaParallelFlusher<E> implements UgaFlusher<E> {

	private RingBuffer<UgaHolder> ringBuffer;

	private UgaParallelEventListener<E> eventListener;

	private WorkerPool<UgaHolder> workerPool;

	private ExecutorService executorService;

	private EventTranslatorOneArg<UgaHolder, E> eventTranslator;


	private UgaParallelFlusher(Builder<E> builder) {
		this.executorService = Executors
				.newFixedThreadPool(builder.threadsNum,
						new ThreadFactoryBuilder()
								.setNameFormat("ParallelFlusher-" + builder.namePrefix + "-pool-%d")
								.build());

		this.eventListener = builder.eventListener;

		RingBuffer<UgaHolder> ringBuffer = RingBuffer.create(
				builder.producerType,
				new UgaHolderEventFactory(),
				builder.bufferSize,
				builder.waitStrategy
		);

		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
		this.ringBuffer = ringBuffer;

		WorkHandler<UgaHolder>[] workHandlers = new WorkHandler[builder.threadsNum];
		for (int i = 0; i < workHandlers.length; i++) {
			workHandlers[i] = new UgaHolderWorkHandler();
		}

		WorkerPool<UgaHolder> workerPool = new WorkerPool<>(ringBuffer,
				sequenceBarrier,
				new UgaHolderExceptionHandler(),
				workHandlers);

		ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

		this.workerPool = workerPool;
	}

	/**
	 * Add events
	 *
	 * @param events events
	 */
	@Override
	@SafeVarargs
	public final void add(@NotNull("events can't be null") E... events) {
		RingBuffer<UgaHolder> temp = ringBuffer;
		if (temp == null) {
			process(this.eventListener, new IllegalStateException("ParallelFlusher is closed"), events);
			return;
		}
		try {
			ringBuffer.publishEvents(this.eventTranslator, events);
		} catch (NullPointerException e) {
			process(this.eventListener, new IllegalStateException("ParallelFlusher is closed"), events);
		}
	}

	/**
	 * try to add an event
	 *
	 * @param events events
	 * @return is it ok?
	 */
	@Override
	@SafeVarargs
	public final Boolean tryAdd(@NotNull("events can't be null") E... events) {
		return null;
	}

	private static <E> void process(UgaParallelEventListener<E> listener, Throwable e, E event) {
		listener.onException(e, -1, event);
	}

	@SafeVarargs
	private static <E> void process(UgaParallelEventListener<E> listener, Throwable e, E... events) {
		for (E event : events) {
			process(listener, e, event);
		}
	}


	/**
	 * Start
	 */
	@Override
	public void start() {
		this.workerPool.start(this.executorService);
	}

	/**
	 * Shut Down
	 */
	@Override
	public void shutdown() {
		RingBuffer<UgaHolder> temp = ringBuffer;
		ringBuffer = null;
		if (temp == null) {
			return;
		}
		if (workerPool != null) {
			workerPool.drainAndHalt();
			workerPool = null;
		}
		if (executorService != null) {
			executorService.shutdown();
			executorService = null;
		}
	}

	/**
	 * valid system is shutdown
	 *
	 * @return is shut down
	 */
	@Override
	public Boolean isShutdown() {
		return this.ringBuffer == null;
	}


	@SuppressWarnings({"unused", "UnusedReturnValue", "FieldCanBeLocal"})
	public static class Builder<E> {

		/**
		 * Default Parallel Producer - Multiple Producers
		 */
		private ProducerType producerType = ProducerType.MULTI;

		/**
		 * The default Buffer Size is 16 KB
		 */
		private Integer bufferSize = 16 * 1024;


		/**
		 * The default thread's num is 1
		 */
		private Integer threadsNum = 1;

		/**
		 * Thread Name's Prefix
		 */
		private String namePrefix = "";

		/**
		 * Wait strategy
		 */
		private WaitStrategy waitStrategy = new BlockingWaitStrategy();


		private UgaParallelEventListener<E> eventListener;


		public Builder<E> setProducerType(@NotNull("producerType can't be null") ProducerType producerType) {
			this.producerType = producerType;
			return this;
		}

		public Builder<E> setBufferSize(@NotNull("bufferSize can't be null") Integer bufferSize) {

			if (bufferSize < 0 || Integer.bitCount(bufferSize) != 1) {
				throw new IllegalArgumentException("Buffer Size must be an integer power of 2");
			}

			this.bufferSize = bufferSize;
			return this;
		}

		public Builder<E> setThreadsNum(@NotNull("threadsNum can't be null") Integer threadsNum) {

			if (threadsNum <= 0) {
				throw new IllegalArgumentException("threadsNum must be greater than zero");
			}

			this.threadsNum = threadsNum;
			return this;
		}

		public Builder<E> setNamePrefix(@NotNull("namePrefix can't be null") String namePrefix) {
			if (namePrefix.isEmpty()) {
				throw new IllegalArgumentException("namePrefix cannot be empty");
			}
			this.namePrefix = namePrefix;
			return this;
		}

		public Builder<E> setWaitStrategy(@NotNull("waitStrategy can't be null") WaitStrategy waitStrategy) {
			this.waitStrategy = waitStrategy;
			return this;
		}

		public Builder<E> setEventListener(@NotNull("eventListener can't be null") UgaParallelEventListener<E> eventListener) {
			this.eventListener = eventListener;
			return this;
		}

		public UgaParallelFlusher<E> build() {
			return new UgaParallelFlusher<E>(this);
		}
	}

	@Data
	private class UgaHolder {

		private E event;

		private void setEvent(E event) {
			this.event = event;
		}
	}

	public interface UgaParallelEventListener<E> {

		void onEvent(E event);

		void onException(Throwable ex, long sequence, E event);
	}


	private class UgaHolderEventFactory implements EventFactory<UgaHolder> {
		@Override
		public UgaHolder newInstance() {
			return new UgaHolder();
		}
	}


	private class UgaHolderWorkHandler implements WorkHandler<UgaHolder> {

		@Override
		public void onEvent(UgaHolder event) throws Exception {
			eventListener.onEvent(event.event);
			event.setEvent(null);
		}

	}

	private class UgaHolderExceptionHandler implements ExceptionHandler<UgaHolder> {

		@Override
		public void handleEventException(Throwable ex, long sequence, UgaHolder event) {
			try {
				eventListener.onException(ex, sequence, event.event);
			} catch (Exception e) {
				//	ignore..
			} finally {
				event.setEvent(null);
			}
		}

		@Override
		public void handleOnStartException(Throwable ex) {
			throw new UnsupportedOperationException(ex);
		}

		@Override
		public void handleOnShutdownException(Throwable ex) {
			throw new UnsupportedOperationException(ex);
		}

	}

}
