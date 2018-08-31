package org.tbwork.roo.client.task;

import java.util.List;

import org.tbwork.roo.client.model.RooBabyClass;

public interface ITaskManager {
	
	public ITask getTaskByName(String taskName);
	
	public void registerTasks();
	
	public void updateTasks(List<RooBabyClass> babies);
}
