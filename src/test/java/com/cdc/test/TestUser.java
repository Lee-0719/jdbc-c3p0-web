package com.cdc.test;

import org.junit.Test;

import com.cdc.entity.User;
import com.cdc.service.UsereService;
import com.cdc.service.impl.UserServiceImpl;

public class TestUser {
	
	private UsereService userService = new UserServiceImpl();

	@Test
	public void testFindLoginUser() {
		User user = new User();
		user.setName("admin");
		user.setPassword("admin");
		user = userService.findLoginUser(user);
		System.out.println(user);
	}
	
	@Test
	public void testFindAll() {
		System.out.println(userService.findAll());
	}
}
