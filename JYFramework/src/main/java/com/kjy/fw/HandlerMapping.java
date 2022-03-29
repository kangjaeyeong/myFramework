package com.kjy.fw;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.kjy.fw.annotation.ServiceController;
import com.kjy.fw.annotation.ServiceProcess;
import com.kjy.fw.dto.MethodInvoker;

/**
 * 클래스 순회하며 어노테이션이 설정된 클래스와 메소드, url 정보를 저장
 * @author kangjaeyeong
 *
 */
public class HandlerMapping {
	
	//url, method, input, output
	Map<String, MethodInvoker> controllerMap = null;
	
	 public void init(){
		 
		 if(controllerMap == null) {
			 controllerMap = new HashMap<>();
			 
			 String sPath = System.getProperty("java.class.path");
			 //C:\Users\kangjaeyeong\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\JYFramework\WEB-INF\classes
			  
			 String sPath2 =  sPath2 =  new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
			 StringBuilder bizPathBuilder = new StringBuilder(sPath2);
			 bizPathBuilder.append(File.separator).append("com").append(File.separator).append("kjy").append(File.separator).append("biz");
			 
			 String bizPath = bizPathBuilder.toString();
			 			 
			 classSearch(bizPath);
		 }
	 }

	private void classSearch(String source) {
		
	    File dir = new File(source);
	    File[] fileList = dir.listFiles();
	
	    if (fileList != null) {
	        try {
	            for (File file : fileList) {
	                
	            	if (file.isDirectory()) {
	            		classSearch(file.getCanonicalPath());
	                } 
	            	else if (file.isFile()) {
	                
	            		String path = file.getPath();
	
	                    if (path.endsWith(".class")) {
	                        
	                    	int classes = path.lastIndexOf("classes");
	                        String substring = path.substring(classes + 8);
	                        String className = substring.split(".class")[0].replace("\\", ".");
	                        
	                        Class<?> aClass = Class.forName(className);
	                        
	                        if (aClass.isAnnotationPresent(ServiceController.class)) {
	                        	Method[] methods = aClass.getMethods();
	                            Arrays.stream(methods).forEach(m -> addPageHandler(aClass, m));
	                        } 
	                    }
	                }
	            }
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	private void addPageHandler(Class claz, Method mthd) {
		
		MethodInvoker methodInvoker = new MethodInvoker();
		methodInvoker.setParameterClzLst(mthd.getParameterTypes());
		methodInvoker.setMethod(mthd);
		methodInvoker.setMethodClass(claz);
		
		if(mthd.isAnnotationPresent(ServiceProcess.class)) {
			ServiceProcess svcPrcsAnno = mthd.getAnnotation(ServiceProcess.class);
			controllerMap.put(svcPrcsAnno.url(), methodInvoker);
		}
		
	}
	
	public Map<String, MethodInvoker> getHandlerMap(){
		return controllerMap;
	}

}
