package org.tbwork.roo.client.lcm;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbwork.roo.client.impl.MotherClient;
import org.tbwork.roo.common.message.c_2_s.PingMessage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener; 

public class PingTask extends TimerTask {

	private static final Logger logger = LoggerFactory.getLogger(PingTask.class);
	 
	@Override
	public void run() { 
		try{ 
			 ping(); 
		}
		catch(Exception e){
			logger.error("Ping failed. Details: {}", e.getMessage());
		} 
	}
	
	private MotherClient client;
	
	public PingTask(MotherClient client){
		this.client = client;
	}


	private void ping(){  
		if(!client.canPing())
		    client.setConnected(false);
		
		if(!client.isConnected())
			client.connect();
		
		if(logger.isInfoEnabled()){
			client.sendMessageWithListeners(new PingMessage(), 
				new ChannelFutureListener(){

					@Override
					public void operationComplete(ChannelFuture future)
							throws Exception {
						if(logger.isDebugEnabled())
							logger.debug("[:)] Ping message is sent successfully.");
					}
		
				}
			);
		}
		else
			client.sendMessage(new PingMessage());
		client.addPingCount();
	}
}
