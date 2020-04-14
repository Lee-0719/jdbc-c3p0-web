package com.cdc.dao.impl;

import java.util.List;

import com.cdc.dao.IUserDao;
import com.cdc.entity.User;
import com.cdc.utils.c3p0.CRUDUtils;

public class UserDaoImpl implements IUserDao {

	@Override
	public int insert(User user) {
		String sql = "insert into t_user(name, password) values (?, ?)";
		int rs = CRUDUtils.commonUpdate(sql, user.getName(), user.getPassword());
		return rs;
	}

	@Override
	public int update(User user) {
		String sql = "update t_user set password=? where id=?";
		int rs = CRUDUtils.commonUpdate(sql, user.getPassword(), user.getId());
		return rs;
	}

	@Override
	public int delete(int id) {
		String sql = "delete from t_user where id=?";
		int rs = CRUDUtils.commonUpdate(sql, id);
		return rs;
	}

	@Override
	public User selectOne(User user) {
		String sql = "select * from t_user where id=?";
		User u = CRUDUtils.commonQueryOne(sql, User.class, user.getId());
		return u;
	}

	@Override
	public List<User> selectAll(User user) {
		String sql = "select * from t_user";
		List<User> list = CRUDUtils.commonQueryList(sql, User.class);
		return list;
	}

}
