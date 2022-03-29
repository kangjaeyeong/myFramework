package com.kjy.fw;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjy.fw.dto.BizSvcCallIn;
import com.kjy.fw.dto.CommonDataVO;
import com.kjy.fw.dto.CommonErrorDataVO;
import com.kjy.fw.dto.Response;
import com.kjy.fw.enums.RequestTypeEnum;
import com.kjy.fw.enums.ResponseTypeEnum;

/**
 * Reflection Method 호출하여 업무 실행
 * Biz controller service executor
 * @author kangjaeyeong
 */
public class BizControllerExecutor {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeService(Response response, BizSvcCallIn bizSvcCallVO) throws Exception {
		
		Object bizResult = new Object();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		CommonDataVO cmmnDataVO = (CommonDataVO) response.getCmnData();
		
		Class[] paramClsList =  bizSvcCallVO.getParamClzList();
		Method mthd = bizSvcCallVO.getMthd();
		String bizDataStr = bizSvcCallVO.getBizInputJsonStr();
		Class mthdClass = bizSvcCallVO.getMethodClass();
		Object mthdInstance = mthdClass.newInstance();
		
		try {
			
			if(paramClsList != null && paramClsList.length > 0) {
	    		Class paramCls = paramClsList[0];
	    		Object paramObj = objectMapper.readValue(bizDataStr, paramCls);
	    		bizResult = mthd.invoke(mthdInstance, paramObj);
	    	}
	    	else {
	    		bizResult = mthd.invoke(mthdInstance);
	    	}
			
			cmmnDataVO.setRequestType	(RequestTypeEnum.RESPONSE.getCd());
			cmmnDataVO.setResponseType	(ResponseTypeEnum.NORMAL.getCd());
			
			response.setBizData(bizResult);
		}
	   	catch(Exception e){
	   		
	   		System.out.println(e);
	   		
	   		CommonErrorDataVO commonErrorDataVO = new CommonErrorDataVO();
	   		
	   		StringWriter sw = new StringWriter();
	   		e.printStackTrace(new PrintWriter(sw));
	   		String errorTrace = sw.toString();
	   		
	   		commonErrorDataVO.setErrTrace(errorTrace);
	   		commonErrorDataVO.setErrMsg(e.getMessage());
	   		
	   		response.setCmnErrData(commonErrorDataVO);
	   		
	   		cmmnDataVO.setRequestType	(RequestTypeEnum.RESPONSE.getCd());
	   		cmmnDataVO.setResponseType	(ResponseTypeEnum.ERROR.getCd());
	   		
	   		throw new Exception(e);
	   	}
	   	
	   	return bizResult;
		
	}
	
}
