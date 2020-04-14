package com.cdc.biz;

import java.util.List;

import com.cdc.entity.User;

public interface IUserBiz {
	
	public List<User> findAll(User user);
	
	public User findOne(User user);

	public int addUser(User user);
	
	public int update(User user);
	
	public int delete(int id);
}
