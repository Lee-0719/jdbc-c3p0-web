package com.cdc.service;

import java.util.List;

import com.cdc.entity.User;

public interface UsereService {
	
	public List<User> findAll();

	public User findLoginUser(User user);
}
