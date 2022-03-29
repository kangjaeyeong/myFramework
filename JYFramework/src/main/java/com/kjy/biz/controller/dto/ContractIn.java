package com.kjy.biz.controller.dto;

import com.kjy.fw.db.ReturnTypeObject;

public class ContractIn extends ReturnTypeObject{

	String cntrctId;
	String cntrctStartDt;
	String cntrctEndDt;
	String cntrctNm;
	
	public String getCntrctId() {
		return cntrctId;
	}
	public void setCntrctId(String cntrctId) {
		this.cntrctId = cntrctId;
	}
	public String getCntrctStartDt() {
		return cntrctStartDt;
	}
	public void setCntrctStartDt(String cntrctStartDt) {
		this.cntrctStartDt = cntrctStartDt;
	}
	public String getCntrctEndDt() {
		return cntrctEndDt;
	}
	public void setCntrctEndDt(String cntrctEndDt) {
		this.cntrctEndDt = cntrctEndDt;
	}
	public String getCntrctNm() {
		return cntrctNm;
	}
	public void setCntrctNm(String cntrctNm) {
		this.cntrctNm = cntrctNm;
	}
	
	
	
}
