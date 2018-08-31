package org.tbwork.roo.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbwork.anole.loader.context.Anole;
import org.tbwork.roo.client.IMotherClient;
import org.tbwork.roo.client.core.RooClientChannelInitializer;
import org.tbwork.roo.client.lcm.ConnectionMonitor;
import org.tbwork.roo.client.lcm.impl.LongConnectionMonitor;
import org.tbwork.roo.common.message.C2SMessage;
import org.tbwork.roo.common.message.Message;
import org.tbwork.roo.common.model.RooBaby;

import com.google.common.base.Preconditions;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MotherClient implements IMotherClient{

	private int port;
	
	public MotherClient(int port){
		this.port = port;
		this.lcm = LongConnectionMonitor.instance(this);
	}
	
	private static final Logger logger = LoggerFactory.getLogger(MotherClient.class);
	
	private volatile boolean started;  
	
	private volatile boolean connected;
	
	private SocketChannel socketChannel = null;
	  
	int clientId = 0; // assigned by the server 
	
	private int ping_count = 0;
	private int MAX_PING_COUNT = 5;
	  
	private ConnectionMonitor lcm;
	
	public static RooBaby updateTask(String taskName, String version){
		RooBaby result = new RooBaby();
		
		return result;
	}
	
	public void addPingCount(){
	    ping_count ++;
    }
    
    public void ackPing(){
    	ping_count --;
    }
    
    public boolean canPing(){
    	return ping_count <= MAX_PING_COUNT;
    }

	@Override
	public void connect() {
		String host = Anole.getProperty("roo.server.host"); 
		if(host == null || port ==0 ){
    		throw new RuntimeException("There is no suitable worker server yet, please try again later!");
    	}
		executeConnect(host, port);
	}

	@Override
	public void close() {
		if(!started) //DCL-1
		{
			synchronized(MotherClient.class)
			{
				if(!started)//DCL-2
				{
					lcm.stop();
					executeClose(); 
				}
			}
		} 
	}
	
	private void executeClose()
	{
		try {
			clientId = 0; //reset 
			socketChannel.closeFuture().sync();
			socketChannel = null;
		} catch (InterruptedException e) {
			logger.error("[:(] Mother client (clientId = {}) failed to close. Inner message: {}", clientId, e.getMessage());
			e.printStackTrace();
		}finally{
			if(!socketChannel.isActive())
			{
				logger.info("[:)] Mother client (clientId = {}) closed successfully !", clientId);			            	
				started = false;
				connected = false;
			}
		}
	}

	@Override
	public void reconnect() {
		 this.close();
		 this.connect();
	}

	/**
	 * Tag each message with current clientId and token before sending.
	 */
	private void tagMessage(C2SMessage msg){
		if(clientId == 0 )
			throw new RuntimeException("Client is invalid (has no valid id)");
		msg.setClientId(clientId); 
	}
	
	@Override
	public void sendMessage(C2SMessage msg) {
		tagMessage(msg);
		socketChannel.writeAndFlush(msg);
	}

	@Override
	public void sendMessageWithListeners(C2SMessage msg, ChannelFutureListener... listeners) {
		ChannelFuture f = socketChannel.writeAndFlush(msg);
		for(ChannelFutureListener item : listeners)
		    f.addListener(item);  
	}
 
	@Override
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@Override
	public boolean isConnected() { 
		return connected;
	}
	
	
	private void executeConnect(String host, int port)
	{ 
		Preconditions.checkNotNull (host    , "host should be null.");
		Preconditions.checkArgument(port > 0, "port should be > 0"  );
		try{
			Bootstrap boot = new Bootstrap();
			EventLoopGroup clientLoop = new NioEventLoopGroup();
			boot.group(clientLoop)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new RooClientChannelInitializer());
			 // Start the client. 
	        ChannelFuture f = boot.connect(host, port).sync();  
	        if (f.isSuccess()) {
	        	socketChannel = (SocketChannel)f.channel(); 
	        	started = true;
	        	connected = true;
	        	logger.info("[:)] The client successfully connected to the remote server with host = '{}' and port = {}", host, port);			            	
	        } 
	    }
	    catch (InterruptedException e) {
	    	logger.error("[:(] The client failed to connect to the remote Anolo hub server with remote host = '{}' and port = ", host, port);
			e.printStackTrace();
	    } 
	}
	    
}

