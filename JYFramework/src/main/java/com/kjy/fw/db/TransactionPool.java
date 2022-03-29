package com.kjy.fw.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author kangjaeyeong
 */
public class TransactionPool extends Transaction {

	@Override
	protected Connection createConnection() throws SQLException {
		
		try {
			/**Was에서 세팅된 Datasource 정보 jndi로 읽어와 DB Connection 생성**/
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/testdb");
			Connection conn = ds.getConnection();
			return conn;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(ex);
		}
	}
	
	

}
