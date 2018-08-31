package org.tbwork.roo.client.core.handler;

import org.tbwork.roo.common.enums.MessageType;
import org.tbwork.roo.common.message.Message;
import org.tbwork.roo.common.message.s_2_c.CallTaskMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class AbstractClientMessageHandler extends SimpleChannelInboundHandler<Message>{

	
	public abstract void tackleWithCallTaskMessage(CallTaskMessage ctm);
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext context, Message msg) throws Exception {
		if(MessageType.S2C_CALL_TASK.equals(msg.getType())){
			tackleWithCallTaskMessage( (CallTaskMessage) msg);
		}
		context.fireChannelRead(msg);
	}

}