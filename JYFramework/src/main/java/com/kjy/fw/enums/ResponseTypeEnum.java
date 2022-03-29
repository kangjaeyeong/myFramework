package com.kjy.fw.enums;

public enum ResponseTypeEnum {
	NORMAL("NM"),
	ERROR("ER");
	
	private String cd;
	
	ResponseTypeEnum(String cd){
		this.cd = cd;
	}

	public String getCd() {
		return cd;
	}
}
