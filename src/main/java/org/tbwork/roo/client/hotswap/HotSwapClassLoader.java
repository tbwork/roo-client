package org.tbwork.roo.client.hotswap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.tbwork.anole.loader.context.Anole;

public class HotSwapClassLoader extends ClassLoader{
 
	private static class SingletonHotSwapClassLoader{
		private static HotSwapClassLoader instance = new HotSwapClassLoader();
	}
	
	public static HotSwapClassLoader getInstance(){
		return SingletonHotSwapClassLoader.instance;
	}
	
	public HotSwapClassLoader(){  
        super(null);  
    }  
      
    @Override  
    protected Class<?> findClass(String name) throws ClassNotFoundException {  
        byte[] data = loadClassData(name);  
        return this.defineClass(name, data, 0, data.length);  
    }  
      
    private byte[] loadClassData(String name) {  
        try {
        	String taskClassPath = Anole.getProperty("candidate.task.path");
        	String filePath = String.format("%s/%s.class", taskClassPath, name);
            FileInputStream is = new FileInputStream(new File(filePath));  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            int b = 0;
            while ((b = is.read()) != -1) {  
                baos.write(b);
            }
            return baos.toByteArray();
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return null;
    }  
  
    @Override  
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {  
        Class cls = null;  
        cls = findLoadedClass(name);  
        if(!name.contains(".Test") && cls == null){  
            cls = getSystemClassLoader().loadClass(name);  
        }else{  
            try{  
                cls = findClass(name);  
            }catch(Exception e){  
                e.printStackTrace();  
            }
        }  
        if (cls == null)  
            throw new ClassNotFoundException(name);  
        if (resolve)  
            resolveClass(cls);  
        return cls;  
    }  
	
     
}
