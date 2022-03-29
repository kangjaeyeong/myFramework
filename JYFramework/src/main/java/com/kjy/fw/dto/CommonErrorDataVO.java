package com.kjy.fw.dto;

public class CommonErrorDataVO {
	
	String errCd;
	String errMsg;
	String errTrace;	
	
	public String getErrCd() {
		return errCd;
	}
	public void setErrCd(String errCd) {
		this.errCd = errCd;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getErrTrace() {
		return errTrace;
	}
	public void setErrTrace(String errTrace) {
		this.errTrace = errTrace;
	}

}
