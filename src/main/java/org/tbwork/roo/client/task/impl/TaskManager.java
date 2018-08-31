package org.tbwork.roo.client.task.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbwork.anole.loader.context.Anole;
import org.tbwork.anole.loader.util.StringUtil;
import org.tbwork.roo.client.hotswap.HotSwapClassLoader;
import org.tbwork.roo.client.model.RooBabyClass;
import org.tbwork.roo.client.task.ITask;
import org.tbwork.roo.client.task.ITaskManager;
import org.tbwork.roo.client.util.DateUtil;

public class TaskManager implements ITaskManager {

	private static final Map<String,ITask> taskMap = new ConcurrentHashMap<String,ITask>();
	
	private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
	
	private ClassLoader classLoader =   HotSwapClassLoader.getInstance();
	
	private static class SingletonTaskManagerFactory{
		private static final ITaskManager instance  = new TaskManager();
	}
	
	public static ITaskManager getInstance(){
		return SingletonTaskManagerFactory.instance;
	}
	
	@Override
	public ITask getTaskByName(String taskName) { 
		return taskMap.get(taskName);
	}

	@Override
	public void registerTasks() {
		//scan all tasks and register.
		synchronized(TaskManager.class){
			try{
				List<String> taskClasses = getAllTaskClassNames();
				for(String item : taskClasses){
					 Class<?> taskClass = classLoader.loadClass(item);
					 ITask taskInstance = (ITask) taskClass.newInstance();
					 taskMap.put(item, taskInstance);
				}
			}
			catch(Exception e){
				logger.error("Fail to register task due to {}", e.getMessage());
			} 
		}
		
	}
	
	
	private List<String> getAllTaskClassNames(){
		List<String> result = new ArrayList<String>();
		String taskClassPath = Anole.getProperty("candidate.task.path");
		File directory = new File(taskClassPath);
		if(!directory.exists())
			throw new RuntimeException("The target directory {} does not exist, please check the property of 'candidate.task.path'");
		File [] files =directory.listFiles(new FilenameFilter() { 
			@Override
			public boolean accept(File dir, String name) {
				return StringUtil.asteriskMatch("*.class", name); 
			}
		}); 
		for(File file : files){
			result.add(file.getName());
		}
		return result;
	}

	@Override
	public void updateTasks(List<RooBabyClass> babies) {
		synchronized(TaskManager.class){
			try{
				move2NewDirectory();
				storeNewTaskFiles(babies);
				registerTasks();
			}
			catch(Exception e){
				logger.error("Update task failed due to {}", e.getMessage(), e);
			} 
		}
	}

	private void move2NewDirectory(){
		String taskClassPath = Anole.getProperty("candidate.task.path");
		File directory = new File(taskClassPath);
		if(!directory.exists())
			throw new RuntimeException("The target directory {} does not exist, please check the property of 'candidate.task.path'");
		String backupDirectoryPath = taskClassPath +"/"+ DateUtil.getDateString();
		File backupDirectory  = new File(backupDirectoryPath);
		if(!backupDirectory.exists()){
			backupDirectory.mkdirs();
		}
		for(File file : directory.listFiles()){
			file.renameTo(new File(backupDirectoryPath+file.getName()));
		}  
	}
	
	private void storeNewTaskFiles(List<RooBabyClass> babies) throws Exception{
		String taskClassPath = Anole.getProperty("candidate.task.path");
		File directory = new File(taskClassPath);
		if(!directory.exists())
			throw new RuntimeException("The target directory {} does not exist, please check the property of 'candidate.task.path'");
		for(RooBabyClass baby : babies){
			String taskFilePath = taskClassPath +"/" + baby.getClassName();
			OutputStream os = new FileOutputStream(new File(taskFilePath));
			os.write(baby.getClassData());
			os.close();
		}
	}
 
	 
}
