package com.kjy.biz.controller.dto;

import com.kjy.fw.db.ReturnTypeObject;

public class ContractOut extends ReturnTypeObject{

	String cntrctId;
	String cntrctStrtDt;
	String cntrctEndDt;
	String cntrctrNm;
	
	public String getCntrctId() {
		return cntrctId;
	}
	public void setCntrctId(String cntrctId) {
		this.cntrctId = cntrctId;
	}
	public String getCntrctStartDt() {
		return cntrctStrtDt;
	}
	public void setCntrctStartDt(String cntrctStartDt) {
		this.cntrctStrtDt = cntrctStartDt;
	}
	public String getCntrctEndDt() {
		return cntrctEndDt;
	}
	public void setCntrctEndDt(String cntrctEndDt) {
		this.cntrctEndDt = cntrctEndDt;
	}
	public String getCntrctrNm() {
		return cntrctrNm;
	}
	public void setCntrctrNm(String cntrctrNm) {
		this.cntrctrNm = cntrctrNm;
	}
	
	
	
}
