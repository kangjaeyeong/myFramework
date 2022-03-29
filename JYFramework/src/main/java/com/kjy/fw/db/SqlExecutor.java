package com.kjy.fw.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqlExecutor {
	
	private static SqlRunner sqlRunner = SqlRunner.getSqlRunner();
	
	
	/**
	 * 업무 영역에서 사용할 Select SQL 실행 공통 메소드
	 * @param <T>
	 * @param key
	 * @param input
	 * @param returnVO
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws SQLException
	 */
	public static <T> List<T> executeSelectSql(String key, Map<String, String> input, T returnVO) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, SQLException {
		Transaction transaction = SQLContext.get();
		transaction.setAutoCommitFalse();
		return sqlRunner.select(transaction, key, input, returnVO);
	}

}
