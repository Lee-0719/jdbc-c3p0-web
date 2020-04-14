package com.cdc.dao.impl;

import java.util.List;

import com.cdc.dao.IUserDao;
import com.cdc.entity.User;
import com.cdc.utils.c3p0.CRUDUtils;

public class UserDaoImpl implements IUserDao {

	public User findLoginUser(User user) {
		String sql = "select * from t_user where name=? and password=?";
		user = CRUDUtils.commonQueryOne(sql, User.class, user.getName(), user.getPassword());
		
		return user;
	}

	@Override
	public List<User> findAll() {
		String sql = "select * from t_user";
		
		return CRUDUtils.commonQueryList(sql, User.class);
	}

}
