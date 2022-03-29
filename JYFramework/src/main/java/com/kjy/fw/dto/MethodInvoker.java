package com.kjy.fw.dto;

import java.lang.reflect.Method;

public class MethodInvoker {
	
	private Method	method;
	private Class[] parameterClzLst;
	private Class 	methodClass;
	
	
	public Class getMethodClass() {
		return methodClass;
	}
	public void setMethodClass(Class methodClass) {
		this.methodClass = methodClass;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Class[] getParameterClzLst() {
		return parameterClzLst;
	}
	public void setParameterClzLst(Class[] parameterClzLst) {
		this.parameterClzLst = parameterClzLst;
	}	

}
