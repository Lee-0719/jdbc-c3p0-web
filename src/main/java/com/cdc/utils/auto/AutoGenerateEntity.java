package com.cdc.utils.auto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @description 代码生成器-通过数据库表生成实体类
 * @author Lee
 * @date 2019-09-16 11:07:58 AM
 */
public class AutoGenerateEntity {

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/exam?useSSL=false&amp;serverTimezone=Hongkong";
	private String user = "root";
	private String pwd = "123";
	
	/**
	 * @Function createEntity
	 * @Description 生成实体类代码
	 * @param packageName 包名，格式如：com.example.entity
	 */
	public void createEntity(String packageName) {
		StringBuffer content = null;
		Map<String, Map<String, String>> classInfoMap = getClassInfo();
		for (Entry<String, Map<String, String>> entry : classInfoMap.entrySet()) {
			content = new StringBuffer("package " + packageName + ";\r\n\r\n");
			content.append("import java.io.Serializable;\r\n\r\n");
			Map<String, String> fieldMap =entry.getValue();
			if (fieldMap.containsValue("Date")) {
				content.append("import java.util.Date;\r\n\r\n");
			}
			content.append("public class " + entry.getKey() + " implements Serializable {\r\n");
			content.append("\tprivate static final long serialVersionUID = 1L;\r\n");
			for (Entry<String, String> fieldEntry : fieldMap.entrySet()) {
				content.append("\tprivate " + fieldEntry.getValue() + " " + fieldEntry.getKey() + ";\r\n");
			}
			//生成get()/set()方法
			content.append(createGetSet(fieldMap));
			content.append("}");
			//生成java文件
			createFile(packageName, entry.getKey(), content.toString());
		}
	}
	
	/**
	 * @Function createFile
	 * @Description 生成java文件
	 */
	private void createFile(String packageName, String fileName, String content) {
		String packagePath = packageName.replace(".", "\\") + "\\";
		String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\" + packagePath;
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		filePath += fileName + ".java";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(content);
			System.out.println("已自动生成实体类" + fileName + "，请刷新目录查看");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private String createGetSet(Map<String, String> fieldMap) {
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, String> fieldEntry : fieldMap.entrySet()) {
			buffer.append("\tpublic " + fieldEntry.getValue() + " get" 
					+ camelCase(fieldEntry.getKey(), true) + "() {\r\n");
			buffer.append("\t\treturn this." + fieldEntry.getKey() + ";\r\n");
			buffer.append("\t}\r\n");
			buffer.append("\tpublic void set" + camelCase(fieldEntry.getKey(), true) + "("
					+ fieldEntry.getValue() + " " + fieldEntry.getKey() + ")" + " {\r\n");
			buffer.append("\t\tthis." + fieldEntry.getKey() + " = " + fieldEntry.getKey() + ";\r\n");
			buffer.append("\t}\r\n");
		}
		return buffer.toString();
	}

	private Map<String, Map<String, String>> getClassInfo() {
//		System.out.println("class info --------------------------");
		Map<String, Map<String, String>> classInfoMap = new HashMap<String, Map<String, String>>();
		for (Entry<String, Map<String, String>> entry : getTableInfo().entrySet()) {
			String className = camelCase(entry.getKey(), true);
//			System.out.println("类名：" + className);
			Map<String, String> columnMap = entry.getValue();
			Map<String, String> fieldMap = new HashMap<String, String>();
			for(Entry<String, String> fieldEntry : columnMap.entrySet()) {
				String fieldName = camelCase(fieldEntry.getKey(), false);
				String fieldType = parseDBType2Java(fieldEntry.getValue());
//				System.out.println(fieldEntry.getKey() + " - " + fieldType + " - " + fieldName);
				fieldMap.put(fieldName, fieldType);
			}
			classInfoMap.put(className, fieldMap);
		}
		return classInfoMap;
	}
	
	private Map<String, Map<String, String>> getTableInfo() {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, pwd);
			DatabaseMetaData meatData = conn.getMetaData();
			ResultSet tables = meatData.getTables(conn.getCatalog(), null, "%", new String[] {"TABLE"});
			Map<String, String> columnMap = null;
			while(tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
//				System.out.println("=================="+tableName+"==================");
				ResultSet columns = meatData.getColumns(conn.getCatalog(), null, tableName, "%");
				columnMap = new HashMap<String, String>();
				while (columns.next()) {
					columnMap.put(columns.getString("COLUMN_NAME"), columns.getString("TYPE_NAME"));
				}
				map.put(tableName, columnMap);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	private String parseDBType2Java(String dbType) {
		switch (dbType.toLowerCase()) {
		case "char":
		case "varchar":
		case "varchar2":
		case "longvarchar":
			return "String";
		case "number":
		case "decimal":
		case "numeric":
		case "float":
		case "double":
			return "double";
		case "int":
		case "integer":
			return "Integer";
		case "bit":
			return "boolean";
		case "date":
		case "time":
		case "datetime":
		case "timestamp":
			return "Date";
		case "tinyint":
			return "byte";
		case "smallint":
			return "short";
		case "bigint":
			return "long";
		case "real":
			return "float";
		default:
			return null;
		}
	}
	/**
	 * @Function camelCase
	 * @Description 驼峰式命名转换
	 * @param str 要转换的字符串
	 * @param flag true-类名，false-属性名
	 */
	private String camelCase(String str, boolean flag) {
		String[] strArr = str.split("_");
		StringBuffer strBuf = new StringBuffer();
		int i = 0;
		if (!flag) {
			i = 1;
			strBuf.append(strArr[0]);
		}
		for( ; i < strArr.length; i++) {
			strBuf.append(strArr[i].substring(0, 1).toUpperCase() + strArr[i].substring(1));
		}
		return strBuf.toString();
	}
}
