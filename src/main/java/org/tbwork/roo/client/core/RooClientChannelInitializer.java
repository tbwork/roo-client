package org.tbwork.roo.client.core;

import org.tbwork.roo.client.core.handler.ExceptionHandler;
import org.tbwork.roo.client.core.handler.MainClientMessageHandler; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RooClientChannelInitializer extends ChannelInitializer {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		 ch.pipeline().addLast(
				 	new ExceptionHandler(),
				 	new ObjectEncoder(),
        		    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),   
        		    new MainClientMessageHandler()
				 );
	}

}
