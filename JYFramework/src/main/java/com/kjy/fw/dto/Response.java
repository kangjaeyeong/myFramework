package com.kjy.fw.dto;

public class Response {
	
	private Object cmnData;
	private Object cmnErrData;
	private Object bizData;
	
	public Object getCmnData() {
		return cmnData;
	}
	public void setCmnData(Object cmnData) {
		this.cmnData = cmnData;
	}
	public Object getBizData() {
		return bizData;
	}
	public void setBizData(Object bizData) {
		this.bizData = bizData;
	}
	public Object getCmnErrData() {
		return cmnErrData;
	}
	public void setCmnErrData(Object cmnErrData) {
		this.cmnErrData = cmnErrData;
	}
	
}
