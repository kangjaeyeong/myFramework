package com.kjy.fw.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**SQL 호출 공통 클래스**/
public class SqlRunner {
	
	private static SqlRunner sqlRunner = new SqlRunner();
	
	public static SqlRunner getSqlRunner() {
		return sqlRunner;
	}
	
	
	SqlObject getSqlQueryFromKey(String key) throws SQLException {

		InputStream sqlInptStrm = null;
		
		try {
			
			//패키지 경로 변경
			key = key.replaceAll("\\.", "/");
			
			//마지막 경로의 SQL 파일명
			int p = key.lastIndexOf("/");

			// SQL 파일로 변경
			String sqlUrl = key.substring(0, p) + ".sql";
			String sqlKey = key.substring(p + 1);

			String[] sqlKeys 	= 	sqlKey.split(",");
			String findSqlKey 	= 	sqlKeys[0];

			ClassLoader cl = SqlRunner.class.getClassLoader();
			sqlInptStrm = cl.getResourceAsStream(sqlUrl);
			
			InputStreamReader sqlInptStrmRdr = new InputStreamReader(sqlInptStrm);
			BufferedReader br = new BufferedReader(sqlInptStrmRdr);

			StringBuffer sql = new StringBuffer();
			
			boolean findSql = false;
			
			/**.sql 파일 순회 시작**/
			while (true) {
				String str = br.readLine();
				if (str == null) {
					break;
				}
				/**SQL 문구 시작**/
				if (str.indexOf("<?Q[") >= 0) {
					if (str.indexOf(findSqlKey) >= 0) {
						findSql = true;
						continue;
					}
				}
				/**SQL 문구 종료**/
				if (str.indexOf("Q?>") >= 0) {
					if (findSql) {
						break;
					}
				}
				if (findSql) {
					sql.append(str + "\n");
				}
			}

			String sqlOrg = sql.toString();
			
			sqlOrg = sqlOrg.trim();
			
			if (sqlOrg.endsWith(";")) {
				sqlOrg = sqlOrg.substring(0, sqlOrg.length() - 1);
			}

			SqlObject sqlRunnerB = new SqlObject();
			sqlRunnerB.sqlOrgText = sqlOrg;
			sqlRunnerB.key = key;

			return sqlRunnerB;

		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(ex.getMessage());
		} 
		finally {

			if (sqlInptStrm != null) {
				try {
					sqlInptStrm.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}

	}

	/**
	 * Query 변수 => ? 치환
	 */
	SqlObject parseSql(SqlObject sqlObject) throws SQLException {

		String sql = sqlObject.sqlOrgText;

		ArrayList<String> param = new ArrayList<String>();

		StringBuffer sqlBuffer = new StringBuffer();

		int fromIndex = -1;
		
		for (int i = 0; i < 1000; i++) {

			int sp = sql.indexOf("#{", fromIndex);
			if (sp == -1) {
				sqlBuffer.append(sql.substring(fromIndex + 1));
				break;
			}

			int ep = sql.indexOf("}", sp + 1);

			param.add(sql.substring(sp + 2, ep).trim());
			sqlBuffer.append(sql.substring(fromIndex + 1, sp) + "?");
			fromIndex = ep;
		}

		sqlObject.param 	= param;
		sqlObject.sqlExctbl = sqlBuffer.toString();

		return sqlObject;
	}

	/**
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	SqlObject getSqlObject(String key) throws SQLException {

		SqlObject sqlObject = getSqlQueryFromKey(key);
		sqlObject = parseSql(sqlObject);

		return sqlObject;
	}

	/**
	 * 실제 select문 호출부
	 * Generic이용하여 결과 값 세팅
	 * @param <T>
	 * @param transaction
	 * @param box
	 * @param key
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused", "deprecation" })
	public <T> List<T> select(Transaction transaction, String key, Map<String, String> input, T returnVO) throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException {

		List<T> outList = new ArrayList();
		
		SqlObject sqlObject = getSqlObject(key);

		Map<String, String> inputMap = input;
		
		PreparedStatement prprdStmnt = transaction.prepareStatement(sqlObject.sqlExctbl);

		String exeSql = sqlObject.sqlExctbl;
		
		for (int i = 0; i < sqlObject.param.size(); i++) {
			String paramKey	= sqlObject.param.get(i);
			String paramVal = inputMap.get(paramKey) ;
			prprdStmnt.setString(i + 1, paramVal);
			exeSql = exeSql.replaceFirst("\\?", "'" + paramVal + "'");
		}

		System.out.println(exeSql);

		ResultSet rs = transaction.getResultSet(prprdStmnt);

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		String[] cols = new String[columnCount];

		for (int i = 0; i < cols.length; i++) {
			cols[i] = metaData.getColumnName(i + 1);
		}
		
		while (rs.next()) {

			String[] recode = new String[columnCount];

			Class returnCls = returnVO.getClass();
			T resultObject =  (T) returnCls.newInstance();
			
			for (int i = 0; i < cols.length; i++) {
				String value = rs.getString(cols[i]);
				Field fld =returnCls.getDeclaredField(cols[i]);
				fld.setAccessible(true);
				fld.set(resultObject, value);
			}
			
			outList.add(resultObject);
		}

		transaction.close();
		
		return outList;
	}

}
