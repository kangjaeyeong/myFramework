package com.kjy.fw.db;

import java.sql.SQLException;


public class SQLContext {
	
	/**트랜잭션 처리를 위한 Static 객체 생성**/
	private static ThreadLocal<Transaction> threadLocal = new ThreadLocal<Transaction>();
	
	/**트랜잭션 정보 Get**/
	public static Transaction get() {
		Transaction transaction = threadLocal.get();
		if (transaction != null) {
			return transaction;
		}
		transaction = Transaction.createTransaction();
		set(transaction);
		return transaction;
	}
	
	/**트랜잭션 정보 Set**/
	public static void set(Transaction transaction) {
		threadLocal.set(transaction);
	}
	
	/**
	 * Commit
	 */
	public static void commit() throws SQLException {
		Transaction transaction = threadLocal.get();
		if (transaction == null) {
			return;
		}
		transaction.commit();
	}

	/**
	 * Rollback
	 */
	public static void rollback() throws SQLException {
		Transaction transaction = threadLocal.get();
		if (transaction == null) {
			return;
		}
		transaction.rollback();
	}
	
	/**
	 * remove
	 */
	public static void remove() {
		Transaction transaction = threadLocal.get();
		if (transaction != null) {
			/**connection 종료**/
			threadLocal.get().closeConnection();
		}
		threadLocal.remove();
	}

}
