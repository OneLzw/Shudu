package com.type.service;

import java.util.List;

import com.type.bean.User;

public interface UserService {
	
	public User getUserById(int userId);    
    
    public void insertUser(User user);    
    
    public void addUser(User user);    
    
    public List<User> getAllUser();
    
    public void deleteUser(int id);
}
