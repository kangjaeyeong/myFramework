package com.kjy.fw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class Transaction {
	
	private Connection conn = null;
	private boolean setAutoCommit = false;
	private ArrayList<Statement> statementList = new ArrayList();
	private ArrayList<ResultSet> resultSetList = new ArrayList();
	
	protected abstract Connection createConnection() throws SQLException;
	
	/**커넥션 획득**/
	public Connection getConnection() throws SQLException {
		if (this.conn == null) {
			this.conn = createConnection();
		}
		return conn;
	}
	
	/**commit**/
	public void commit() throws SQLException {
		if (this.conn == null) {
			return;
		}
		this.close();
		this.conn.commit();
	}

	/**rollback**/
	public void rollback() throws SQLException {

		if (this.conn == null) {
			return;
		}
		
		if(!this.conn.isClosed()) {
			this.conn.rollback();
			this.close();
		}
		
	}
	
	/**트랜잭션 생성**/
	public static Transaction createTransaction() {
		return new TransactionPool();
	}	

	/**Statement 생성**/
	public PreparedStatement prepareStatement(String sqlQuery) throws SQLException {
		if (this.conn == null) {
			this.conn = getConnection();
		}
		PreparedStatement ps = this.conn.prepareStatement(sqlQuery);
		statementList.add(ps);
		return ps;
	}


	public Statement createStatement() throws SQLException {
		if (this.conn == null) {
			this.conn = getConnection();
		}
		Statement st = this.conn.createStatement();
		statementList.add(st);
		return st;
	}
	
	public ResultSet getResultSet(PreparedStatement ps) throws SQLException {
		ResultSet rs = ps.executeQuery();
		resultSetList.add(rs);
		return rs;
	}


	/**종료시 보관 자원 모두 반환**/
	public void close() throws SQLException {
		
		conn.close();
		
		for (int i = 0; i < resultSetList.size(); i++) {
			try {
				resultSetList.get(i).close();
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		resultSetList.clear();
		
		for (int i = 0; i < statementList.size(); i++) {
			try {
				statementList.get(i).close();
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		statementList.clear();
	}

	
	/**
	 * Auto Commit을 False로 설정 
	 * 트랜잭션 시작
	 * @throws SQLException
	 */
	public void setAutoCommitFalse() throws SQLException {
		
		if (setAutoCommit) {
			return;
		}
		
		if (this.conn == null) {
			this.conn = createConnection();
		}
		
		setAutoCommit = true;
		conn.setAutoCommit(false);
	}
	

	public void closeConnection() {
		try {
			if (conn == null) {
				return;
			}
			this.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
		

}
