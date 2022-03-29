package com.kjy.fw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjy.fw.db.SQLContext;
import com.kjy.fw.db.Transaction;
import com.kjy.fw.dto.BizSvcCallIn;
import com.kjy.fw.dto.CommonDataVO;
import com.kjy.fw.dto.CommonErrorDataVO;
import com.kjy.fw.dto.MethodInvoker;
import com.kjy.fw.dto.Response;
import com.kjy.fw.enums.RequestTypeEnum;
import com.kjy.fw.enums.ResponseTypeEnum;

public class DispatcherServlet extends HttpServlet{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private static final long serialVersionUID = 1L;
	private static HandlerMapping handlerMapping;
	
	public void init() {
		handlerMapping = new HandlerMapping();
		handlerMapping.init();
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("EUC-KR");
        process(request, response);
    }
    
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	
    	/**Servlet Response**/
    	Response servletResponse = new Response();

    	/**내부 공통VO**/
    	CommonDataVO commonDataVO = null;
    	
    	/**
    	 * -----------------------------------------------------------------------------------------------------------
    	 * Request 데이터 분석
    	 * CommonData 영역, BizData 분리
    	 * -----------------------------------------------------------------------------------------------------------
    	 */
    	String requestURI = request.getRequestURI();
    	    	
    	BufferedReader bf = request.getReader();
    	
    	Object obj = JSONValue.parse(bf);
    	JSONObject jobj = (JSONObject)obj;
    	
    	logger.info("Input Json Data = {}", jobj );
    	    	
    	JSONObject	bizData 	= null;
		String 		bizDataStr 	= null;
    	
    	
		if(jobj == null) {
			return;
		}
		
		/**공통 Data**/
		JSONObject commonDataJson = (JSONObject) jobj.get("commonData");
		String commonDataStr = commonDataJson.toJSONString();
		commonDataVO = objectMapper.readValue(commonDataStr, CommonDataVO.class);
		servletResponse.setCmnData(commonDataVO);
		
		/**업무 Data**/
		JSONObject bizDataJson = (JSONObject) jobj.get("bizData");
		bizDataStr = bizDataJson.toJSONString();
    	
    	/**
    	 * -----------------------------------------------------------------------------------------------------------
    	 * HandlerMapping 통하여 Controller 호출 메소드 정보 획득
    	 * -----------------------------------------------------------------------------------------------------------
    	 */
    	Map<String, MethodInvoker> hndlrMap =  handlerMapping.getHandlerMap();
    	MethodInvoker methodInvoker = hndlrMap.get(requestURI);
    	
    	if(methodInvoker == null) {
    		
    		CommonErrorDataVO commonErrorDataVO = new CommonErrorDataVO(); 
    				
    		commonErrorDataVO.setErrCd		(	"ERR001"	);
    		commonErrorDataVO.setErrMsg		(	"No resource that can access with requested URI"	);
    		
    		commonDataVO.setRequestType		(	RequestTypeEnum.RESPONSE.getCd()					);
    		commonDataVO.setResponseType	(	ResponseTypeEnum.ERROR.getCd()						);
    		
    		servletResponse.setCmnErrData(commonErrorDataVO);
    		
    		_returnResponse(response, servletResponse);
    		
    		return;
    	}
    	
    	
    	/**
    	 * Transaction Pool에서 Transaction Connection
    	 */
    	Transaction transaction = Transaction.createTransaction();
    	SQLContext.set(transaction);
    	
    	
    	
    	
    	/**
    	 * -----------------------------------------------------------------------------------------------------------
    	 * Biz 서비스 호출
    	 * -----------------------------------------------------------------------------------------------------------
    	 */
    	try {
    		
    		Method mthd = methodInvoker.getMethod();
        	@SuppressWarnings("rawtypes")
    		Class[] paramClsList = methodInvoker.getParameterClzLst();
        	Class methodClass = methodInvoker.getMethodClass();
        	
    		/**업무 호출 VO**/
        	BizSvcCallIn bizSvcCallIn = new BizSvcCallIn();
        	bizSvcCallIn.setMthd			(	mthd			);
        	bizSvcCallIn.setParamClzList	(	paramClsList	);
        	bizSvcCallIn.setBizInputJsonStr	(	bizDataStr		);
        	bizSvcCallIn.setMethodClass		(	methodClass		);
        	/**업무 실행 Executor**/
    		BizControllerExecutor bizControllerExecutor = new BizControllerExecutor();
    		bizControllerExecutor.executeService(servletResponse , bizSvcCallIn);
    		
    		/**Transaction commit**/
    		SQLContext.commit();
    	
	    } 
    	catch (Exception e) {
	    	
	    	try {
	    		/**Transaction Rollback**/
	    		SQLContext.rollback();
			} 
	    	catch (SQLException e1) {
				e1.printStackTrace();
			}
	    	
	    }
    	finally {
    		SQLContext.remove();
		}
    	
    	
    	/**
    	 * -----------------------------------------------------------------------------------------------------------
    	 * Output 조립 후 전달
    	 * -----------------------------------------------------------------------------------------------------------
    	 */
    	_returnResponse(response, servletResponse);
    
	 }
    
	
    private void _returnResponse(HttpServletResponse response, Response servletResponse) throws IOException {
    	
    	String returnVal = objectMapper.writeValueAsString(servletResponse);
    	
    	logger.info("Output Json Data = {}", returnVal );
    	
    	response.setContentType("application/json");
    	response.setCharacterEncoding("EUC-KR");
    	response.getWriter().write(returnVal);
    	
    }

}
