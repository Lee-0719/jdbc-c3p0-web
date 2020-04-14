package com.cdc.biz.impl;

import java.util.List;

import com.cdc.biz.IUserBiz;
import com.cdc.dao.impl.UserDaoImpl;
import com.cdc.entity.User;

public class UserBizImpl implements IUserBiz {
	
	private UserDaoImpl userDao = new UserDaoImpl();

	@Override
	public int addUser(User user) {
		
		return userDao.insert(user);
	}

	@Override
	public int update(User user) {
		
		return userDao.update(user);
	}

	@Override
	public int delete(int id) {
		
		return userDao.delete(id);
	}

	@Override
	public List<User> findAll(User user) {
		
		return userDao.selectAll(user);
	}

	@Override
	public User findOne(User user) {
		
		return userDao.selectOne(user);
	}

}
