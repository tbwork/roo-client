package org.tbwork.roo.client.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbwork.roo.client.context.IContextService;
import org.tbwork.roo.client.context.impl.ContextService;
import org.tbwork.roo.client.model.TaskExecuteContext;
import org.tbwork.roo.common.message.s_2_c.CallTaskMessage;
import org.tbwork.roo.common.model.ITaskParameter;

public class MainClientMessageHandler extends AbstractClientMessageHandler{

	private final IContextService contextService = ContextService.getContextService();
	private static final Logger logger = LoggerFactory.getLogger(MainClientMessageHandler.class);
	@Override
	public void tackleWithCallTaskMessage(CallTaskMessage ctm) {
		// 1 个task 1种参数组合，同一时间只允许1个线程在运行。
		String taskName = ctm.getTaskName();
		ITaskParameter taskParameter = ctm.getTaskParameter(); 
		TaskExecuteContext taskExecuteContext = contextService.getExecuteContext(taskName, taskParameter);
		try{
			taskExecuteContext.lock();
			//TODO 
		}
		catch(Exception e){
			logger.warn("Task execution failed due to {}", e.getMessage(), e);
		}
		finally{
			taskExecuteContext.unlock();
		} 
	}

}
