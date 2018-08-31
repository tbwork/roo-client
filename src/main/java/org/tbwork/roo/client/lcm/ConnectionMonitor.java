package org.tbwork.roo.client.lcm;

/**
 * Connection monitor watches the connection between the roo client
 * and the father server all the time. It sends ping message to the
 * father server regularly, once it finds the connection is broken,
 * it will try to reconnect to the server.
 * 
 * @author tommy.tang
 */
public interface ConnectionMonitor {

	public void start();
	
	public void stop();
	
	public void restart();
	 
}
