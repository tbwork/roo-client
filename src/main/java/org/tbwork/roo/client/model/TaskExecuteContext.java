package org.tbwork.roo.client.model;

import java.util.concurrent.locks.ReentrantLock;

import org.tbwork.roo.common.model.ITaskParameter;

import lombok.Data;

@Data
public class TaskExecuteContext extends ReentrantLock{

	private String taskName; 
	
	private ITaskParameter taskParameter;
	
	private String parameterMD5;
	
}
