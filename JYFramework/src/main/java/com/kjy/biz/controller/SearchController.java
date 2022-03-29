package com.kjy.biz.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kjy.biz.controller.dto.ContractIn;
import com.kjy.biz.controller.dto.ContractOut;
import com.kjy.fw.annotation.ServiceController;
import com.kjy.fw.annotation.ServiceProcess;
import com.kjy.fw.db.SqlExecutor;

@ServiceController
public class SearchController{

	@ServiceProcess(url="/contract")
	public ContractOut contractSearch(ContractIn cntrctIn) throws Exception {
		
		ContractOut out = new ContractOut();
		
		String path = "com.kjy.biz.db.test.selectContract";
		Map<String, String> input = new HashMap();
		input.put("cntrctId", cntrctIn.getCntrctId());
		
		try {
			
			List<ContractOut> list = SqlExecutor.executeSelectSql(path, input, out);
			
			if(list!= null && list.size() > 0) out = list.get(0);
			
		} 
		catch (IllegalArgumentException | IllegalAccessException | InstantiationException | NoSuchFieldException
				| SecurityException | SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception();
		}
		
		return out;
	}

}
