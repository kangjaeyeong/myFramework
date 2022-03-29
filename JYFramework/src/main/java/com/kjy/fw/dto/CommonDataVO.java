package com.kjy.fw.dto;

public class CommonDataVO {
	
	String guid;			/**거래ID**/
	String trxDatetime;		/**거래일시**/
	String requestType;		/**요청,응답구분**/
	String responseType;	/**응답 정상오류 여부**/
	String chnlCd;			/**채널구분**/
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getTrxDatetime() {
		return trxDatetime;
	}
	public void setTrxDatetime(String trxDatetime) {
		this.trxDatetime = trxDatetime;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getChnlCd() {
		return chnlCd;
	}
	public void setChnlCd(String chnlCd) {
		this.chnlCd = chnlCd;
	}

	
}
