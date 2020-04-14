package com.cdc.dao;

import java.util.List;

import com.cdc.entity.User;

public interface IUserDao {
	
	public User selectOne(User user);
	
	public List<User> selectAll(User user);

	public int insert(User user);
	
	public int update(User user);
	
	public int delete(int id);
}
