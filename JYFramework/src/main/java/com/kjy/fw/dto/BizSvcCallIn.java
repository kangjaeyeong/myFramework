package com.kjy.fw.dto;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public class BizSvcCallIn {
	
	private Method mthd;			/**업무 호출 Method**/
	private Class[] paramClzList;	/**컨트롤러 파라미터 리스트**/
	private String bizInputJsonStr;	/**Input jsonData**/
	private Class methodClass;		/**업모 호출하는 메소드 클래스**/
	
	
	public Class getMethodClass() {
		return methodClass;
	}
	public void setMethodClass(Class methodClass) {
		this.methodClass = methodClass;
	}
	public Method getMthd() {
		return mthd;
	}
	public void setMthd(Method mthd) {
		this.mthd = mthd;
	}
	
	public Class[] getParamClzList() {
		return paramClzList;
	}
	public void setParamClzList(Class[] paramClzList) {
		this.paramClzList = paramClzList;
	}
	public String getBizInputJsonStr() {
		return bizInputJsonStr;
	}
	public void setBizInputJsonStr(String bizInputJsonStr) {
		this.bizInputJsonStr = bizInputJsonStr;
	}
	
	

}
