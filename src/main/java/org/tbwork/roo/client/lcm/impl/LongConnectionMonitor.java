package org.tbwork.roo.client.lcm.impl;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbwork.roo.client.GlobalConfig;
import org.tbwork.roo.client.impl.MotherClient;
import org.tbwork.roo.client.lcm.ConnectionMonitor;
import org.tbwork.roo.client.lcm.PingTask; 

public class LongConnectionMonitor implements ConnectionMonitor{

	private static final Logger logger = LoggerFactory.getLogger(LongConnectionMonitor.class);
	
	public static class LCMFactory {
		private static final LongConnectionMonitor lcMonitor = new LongConnectionMonitor(); 
	}
	
	private Timer timer = null;
	public volatile int unreceived_count = 0;
	private LongConnectionMonitor() { }
	
	private MotherClient mc;
	
	public static LongConnectionMonitor instance(MotherClient mc){
		LCMFactory.lcMonitor.mc = mc;
		return LCMFactory.lcMonitor;
	}
	
	@Override
	public void start() { 
		timer = new Timer();
		timer.schedule(new PingTask(mc), GlobalConfig.PING_DELAY, GlobalConfig.PING_INTERVAL);
	}

	@Override
	public void stop() { 
		timer.cancel();
		timer = null;
	}

	@Override
	public void restart() {
		stop();
		start();
	}
	 
	
}
