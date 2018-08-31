package org.tbwork.roo.client;

import org.tbwork.roo.common.message.C2SMessage;

import io.netty.channel.ChannelFutureListener;

public interface IMotherClient {
 
	/**
	 * Connect to the server.
	 */
	public void connect();
	
	/**
	 * Disconnect with the server and close application.
	 */
	public void close();

	/**
	 * Close and reconnect.
	 */
	public void reconnect();
	
	/**
	 * Send a message to the server.
	 */
	public void sendMessage(C2SMessage msg);
	
	/**
	 * Send message to the server and notify specified listeners after sending.
	 */
	public void sendMessageWithListeners(C2SMessage msg, ChannelFutureListener ... listeners);
	  
    /**
     * Assume the ping is not received.
     */
    public void addPingCount();
    
    /**
     * Ack of Ping, indicate that the ping is received.
     */
    public void ackPing();
    
    /**
     * Whether the connection is valid.
     */
    public boolean canPing();
    
    /**
     * Set the connected status. 
     */
    public void setConnected(boolean connected);
    
    /**
     * Check the connection status. 
     */
    public boolean isConnected();
	
}
