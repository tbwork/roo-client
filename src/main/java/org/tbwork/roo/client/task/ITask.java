package org.tbwork.roo.client.task;

import org.tbwork.roo.common.model.ITaskParameter;
import org.tbwork.roo.common.model.ITaskResult;

public interface ITask<R extends ITaskResult, P extends ITaskParameter> { 
	 public R execute(P p);
}