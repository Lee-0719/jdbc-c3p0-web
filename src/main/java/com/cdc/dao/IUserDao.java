package com.cdc.dao;

import java.util.List;

import com.cdc.entity.User;

public interface IUserDao {
	
	public List<User> findAll();
	
	public User findLoginUser(User user);

}
