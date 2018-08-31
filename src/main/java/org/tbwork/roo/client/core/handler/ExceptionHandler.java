package org.tbwork.roo.client.core.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * Deal with all exceptions.
 * @author tommy.tang
 */  
public class ExceptionHandler extends ChannelHandlerAdapter {

	static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);  
	
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
    	if(cause instanceof IOException) { 
    		logger.warn("The Anole server (address = {}) disconnected initiatively! ", ctx.channel().remoteAddress());
    	}
    	else {
    		logger.warn("Exceptions caught, more details: ");
    		cause.printStackTrace();
    	} 
        ctx.close();
    }
     
}