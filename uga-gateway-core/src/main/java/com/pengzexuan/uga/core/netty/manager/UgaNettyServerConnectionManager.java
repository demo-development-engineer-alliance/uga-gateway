package com.pengzexuan.uga.core.netty.manager;

import com.pengzexuan.uga.common.utils.RemotingHelper;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class UgaNettyServerConnectionManager extends ChannelDuplexHandler {

	/**
	 * Called when a new channel is registered to the Event Loop
	 *
	 * @param ctx ctx
	 * @throws Exception Exception
	 */
	@Override
	public void channelRegistered(@NotNull ChannelHandlerContext ctx) throws Exception {
		String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
		log.debug("NETTY SERVER PIPELINE: channelRegistered {}", remoteAddr);
		super.channelRegistered(ctx);
	}

	/**
	 * Called when a channel logs out of the Event Loop
	 *
	 * @param ctx ctx
	 * @throws Exception Exception
	 */
	@Override
	public void channelUnregistered(@NotNull ChannelHandlerContext ctx) throws Exception {
		String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
		log.debug("NETTY SERVER PIPELINE: channelUnregistered {}", remoteAddr);
		super.channelUnregistered(ctx);
	}

	/**
	 * Called when a channel is active
	 *
	 * @param ctx ctx
	 * @throws Exception Exception
	 */
	@Override
	public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
		String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
		log.debug("NETTY SERVER PIPELINE: channelActive {}", remoteAddr);
		super.channelActive(ctx);
	}

	/**
	 * Called when a channel is inactive
	 *
	 * @param ctx ctx
	 * @throws Exception Exception
	 */
	@Override
	public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
		String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
		log.debug("NETTY SERVER PIPELINE: channelInactive {}", remoteAddr);
		super.channelInactive(ctx);
	}


	/**
	 * Called when a user event is triggered
	 *
	 * @param ctx ctx
	 * @param evt event
	 * @throws Exception Exception
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.ALL_IDLE)) {
				String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
				log.warn("NETTY SERVER PIPELINE: userEventTriggered: IDLE {}", remoteAddr);
				ctx.channel().close();
			}
		}
		ctx.fireUserEventTriggered(evt);
	}

	/**
	 * Called when an exception occurs while processing an event
	 *
	 * @param ctx ctx
	 * @param cause Exception Cause
	 * @throws Exception Exception
	 */
	@Override
	public void exceptionCaught(@NotNull ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
		log.warn("NETTY SERVER PIPELINE: remoteAddr： {}, exceptionCaught {}", remoteAddr, cause);
		ctx.channel().close();
	}

}