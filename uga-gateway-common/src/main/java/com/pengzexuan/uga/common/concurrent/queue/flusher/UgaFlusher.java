package com.pengzexuan.uga.common.concurrent.queue.flusher;

import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"unused"})
public interface UgaFlusher<E> {

	/**
	 * Add event
	 *
	 * @param events events
	 */
	@SuppressWarnings("unchecked")
	void add(@NotNull("event can't be null") E... events);


	/**
	 * try to add an event
	 *
	 * @param events events
	 * @return is it ok?
	 */
	@SuppressWarnings("unchecked")
	Boolean tryAdd(@NotNull("events can't be null") E... events);

	/**
	 * Start
	 */
	void start();

	/**
	 * Shut down
	 */
	void shutdown();

	/**
	 * valid system is shutdown
	 *
	 * @return is shut down
	 */
	Boolean isShutdown();
}