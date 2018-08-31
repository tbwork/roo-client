package org.tbwork.roo.client.context;

import org.tbwork.roo.client.model.TaskExecuteContext;
import org.tbwork.roo.common.model.ITaskParameter;

public interface IContextService {

	public TaskExecuteContext getExecuteContext(String taskName, ITaskParameter taskParameter); 
}
