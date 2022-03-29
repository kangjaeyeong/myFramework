package com.kjy.fw.enums;

public enum RequestTypeEnum {
	
	REQUEST("S"),
	RESPONSE("R");
	
	private String code;
	
	RequestTypeEnum(String code){
		this.code = code;
	}
	
	public String getCd() {
		return code;
	}

}
