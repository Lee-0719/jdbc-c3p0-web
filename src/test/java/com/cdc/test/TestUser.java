package com.cdc.test;

import java.util.List;

import org.junit.Test;

import com.cdc.biz.impl.UserBizImpl;
import com.cdc.entity.User;


public class TestUser {
	
	private UserBizImpl userBizImpl = new UserBizImpl();
	
	@Test
	public void testAdd() {
		User user = new User();
		user.setName("zhaoliu");
		user.setPassword("123");
		int rs = userBizImpl.addUser(user);
		System.out.println(rs);
	}

	@Test
	public void testUpdate() {
		User user = new User();
		user.setPassword("11111");
		user.setId(2);
		int rs = userBizImpl.update(user);
		System.out.println(rs);
	}
	
	@Test
	public void testDel() {
		int rs = userBizImpl.delete(2);
		System.out.println(rs);
	}
	
	@Test
	public void testFindAll() {
		List<User> list = userBizImpl.findAll(null);
		for(User u : list) {
			System.out.println(u);
		}
	}
	
	@Test
	public void testFindOne() {
		User user = new User();
		user.setId(2);
		System.out.println(userBizImpl.findOne(user));
		
	}
}
