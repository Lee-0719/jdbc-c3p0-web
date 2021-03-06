package com.cdc.utils.c3p0;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据库增删改查工具类
 * */
public class CRUDUtils {

	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	
	//insert, update, delete
	/**
	 * @Function commonUpdate
	 * @Description 插入，更新，删除
	 * @param sql 执行的SQL语句
	 * @param objects SQL语句中的字段值
	 */
	public static int commonUpdate(String sql, Object...objects) {
		conn = C3P0Utils.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			if(objects != null && objects.length > 0) {
				for(int i = 0; i < objects.length; i++) {
					ps.setObject(i+1, objects[i]);
				}
			}
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Utils.close(ps, conn);
		}
		return 0;
	}
	
	//selectOne
	/**
	 * @Function commonQueryOne
	 * @Description 查找单条记录
	 * @param sql 执行的SQL语句
	 * @param cls 实体类对象
	 * @param objects SQL语句中的限制条件
	 */
	public static <E> E commonQueryOne(String sql, Class<E> cls, Object...objects) {
		conn = C3P0Utils.getConnection();
		E entity = null;
		try {
			ps = conn.prepareStatement(sql);
			if(objects != null && objects.length > 0) {
				for(int i = 0; i < objects.length; i++) {
					ps.setObject(i+1, objects[i]);
				}
			}
			//获取结果集
			rs = ps.executeQuery();  
			
			/**
			 * 以下通过数据库表中字段去查找实体类中的属性名
			 */
			//获取结果集中对象的数量、列名等
			ResultSetMetaData rsmd = rs.getMetaData();  
			//获取字段数
			int columnCount = rsmd.getColumnCount();  
			while(rs.next()) {
				//ͨ通过反射获取实体类对象
				entity = cls.newInstance();  
				for(int i = 0; i < columnCount; i++) {
					//获取字段名称
					String columnName = rsmd.getColumnName(i+1); 
					//获取该字段对应的值ֵ
					Object columnValue = rs.getObject(columnName);  
					//通过字段名获取属性，try{名称不匹配}catch{到配置文件查找对应属性名}
					Field field = null;
					try{
						field = cls.getDeclaredField(columnName); 
					}catch (Exception e){
						Properties p = new Properties();
						String mappingFile = cls.getSimpleName() + "Mapping.properties";
						System.out.println(mappingFile);
						InputStream is = CRUDUtils.class.getClassLoader().getResourceAsStream(mappingFile);
						try{
							p.load(is);
							String fieldName = p.getProperty(columnName); //key=value -> user_name=username
							field = cls.getDeclaredField(fieldName);
						}catch(IOException ioe){
							ioe.printStackTrace();
						}
					}
					//将私有属性非可访问设置为可访问
					field.setAccessible(true);  
					//给实体类中的属性赋值ֵ
					field.set(entity, columnValue);  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			C3P0Utils.close(rs, ps, conn);
		}
		return entity;
	}
	
	//selectAll
	/**
	 * @Function commonQueryList
	 * @Description 查找多条记录
	 * @param sql 执行的SQL语句
	 * @param cls 实体类对象
	 * @param objects SQL语句中的限制条件
	 */
	public static <E> List<E> commonQueryList(String sql,  Class<E> cls, Object...objects) {
		conn = C3P0Utils.getConnection();
		List<E> list = new ArrayList<E>();
		E entity = null;
		try {
			ps = conn.prepareStatement(sql);
			if(objects != null && objects.length > 0) {
				for(int i = 0; i < objects.length; i++) {
					ps.setObject(i+1, objects[i]);
				}
			}
			rs = ps.executeQuery();  
			
			/**
			以下通过实体类属性名去查找数据库表中对应字段名
			Field[] fields = cls.getDeclaredFields();
			while(rs.next()) {
				entity = cls.newInstance();
				for(Field field : fields) {
					String fieldName = field.getName();
					Object fieldValue = rs.getObject(fieldName);
					field.setAccessible(true);
					field.set(entity, fieldValue);
				}
				list.add(entity);
			}
			*/
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				entity = cls.newInstance();
				for(int i = 0; i < columnCount; i++) {
					String columnName = rsmd.getColumnName(i+1);
					Object columnValue = rs.getObject(columnName);
					//通过字段名获取属性，try{名称不匹配}catch{到配置文件查找对应名称}
					Field field = null;
					try{
						field = cls.getDeclaredField(columnName); 
					}catch (Exception e){
						Properties p = new Properties();
						String mappingFile = cls.getSimpleName() + "Mapping.properties";
						InputStream is = CRUDUtils.class.getClassLoader().getResourceAsStream(mappingFile);
						try{
							p.load(is);
							String fieldName = p.getProperty(columnName); //key=value -> user_name=username
							field = cls.getDeclaredField(fieldName);
						}catch(IOException ioe){
							ioe.printStackTrace();
						}
					}
					field.setAccessible(true);
					field.set(entity, columnValue);
				}
				list.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			C3P0Utils.close(rs, ps, conn);
		}
		return list;
	}
}
