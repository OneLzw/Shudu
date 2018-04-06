package com.type.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.type.bean.User;
import com.type.dao.UserMapper;
import com.type.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Resource
	private UserMapper userDao;

	@Override
	public User getUserById(int userId) {
		User user = userDao.queryByPrimaryKey(userId);
		return user;
	}

	@Override
	public void insertUser(User user) {
		userDao.insertUser(user);
	}

	@Override
	public void addUser(User user) {
		 userDao.insertUser(user);
	}

	@Override
	public List<User> getAllUser() {
		List<User> allUser = userDao.getAllUser();
		return allUser;  
	}

	@Override
	public void deleteUser(int id) {
		userDao.deleteByPrimaryKey(id);
	}

}
