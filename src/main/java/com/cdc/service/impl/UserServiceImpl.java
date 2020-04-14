package com.cdc.service.impl;

import java.util.List;

import com.cdc.dao.IUserDao;
import com.cdc.dao.impl.UserDaoImpl;
import com.cdc.entity.User;
import com.cdc.service.UsereService;

public class UserServiceImpl implements UsereService {
	
	private IUserDao userDao = new UserDaoImpl();

	@Override
	public User findLoginUser(User user) {
		
		return userDao.findLoginUser(user);
	}

	@Override
	public List<User> findAll() {
		
		return userDao.findAll();
	}

}
