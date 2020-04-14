package com.cdc.test;

import com.cdc.entity.User;

public class Test {

	@org.junit.Test
	public void test() {
		Class<User> clazz = User.class;
		System.out.println(clazz.getSimpleName());
	}
}
