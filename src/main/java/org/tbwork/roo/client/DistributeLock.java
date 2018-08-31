package org.tbwork.roo.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributeLock {

	
	public static void main(String[] args) throws Exception {
		
	   System.out.println(1>>2);
		
	}

	
	public void testCurator() throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3); 
		
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:1111", retryPolicy);
		
		client.start();
		
		InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
		
		mutex.acquire();
		
		System.out.println("XXXXXX");
	}
}
