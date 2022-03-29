package com.kjy.fw.db;

import java.util.List;
/**
 * SQL Parsing 및 실행을 위한 SQL Object
 * @author kangjaeyeong
 *
 */
public class SqlObject {

	String key = null;				
	String sqlOrgText = null;		//읽어들인 원본 SQL
	String sqlExctbl = null;		//sql 최종 실행문
	List<String> param = null;		//parameter
	
}
