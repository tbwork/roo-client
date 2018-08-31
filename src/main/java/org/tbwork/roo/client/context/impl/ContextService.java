package org.tbwork.roo.client.context.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tbwork.roo.client.context.IContextService;
import org.tbwork.roo.client.model.TaskExecuteContext;
import org.tbwork.roo.client.util.MD5Util;
import org.tbwork.roo.common.model.ITaskParameter;

import com.alibaba.fastjson.JSON;

public class ContextService implements IContextService{

	private static class SingletonContextServiceFactory{
		private static final IContextService contextService = new ContextService();
	}
	
	private static final Map<String, TaskExecuteContext> contextMap = new ConcurrentHashMap<String, TaskExecuteContext>();
	 
	@Override
	public TaskExecuteContext getExecuteContext(String taskName, ITaskParameter taskParameter) {
		String jsonParameter = JSON.toJSONString(taskParameter);
		StringBuilder sb = new StringBuilder();
		sb.append(taskName).append("-").append(jsonParameter);
		String invokerKey = MD5Util.MD5(sb.toString());
		if(!contextMap.containsKey(invokerKey)){
			synchronized(ContextService.class){
				if(!contextMap.containsKey(invokerKey)){
					TaskExecuteContext tec = new TaskExecuteContext();
					contextMap.put(invokerKey, tec);
				}
			}
		}
		TaskExecuteContext taskExecuteContext = contextMap.get(invokerKey); 
		return taskExecuteContext;
	}
 
	public static IContextService getContextService() {
		// TODO Auto-generated method stub
		return SingletonContextServiceFactory.contextService;
	} 
	
}
