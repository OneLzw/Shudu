package com.type.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.type.bean.User;
import com.type.service.UserService;



@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@RequestMapping("oneuser")
	public String getOneUser(Model model) {
		model.addAttribute("name", "hello world");
		return "jsp/user";
	}
	
	@RequestMapping("addUser")
	public String addUser(@RequestParam("name")String name , 
			@RequestParam("password")String password) {
		User user = new User();
		user.setName(name);
		user.setAge(5);
		user.setPassword(password);
		System.out.println("one change");
		userService.addUser(user);
		return "jsp/user";
	}
	
	@RequestMapping("userList")
	public String addOneUser (Model model , @RequestParam("name")String name , @RequestParam("age")int age) {
		System.out.println("name:" + name + "  age:" + age);
		List<User> allUser = userService.getAllUser();
		if (allUser != null) {
			System.out.println("size : " + allUser.size());
		}
		model.addAttribute("userList", allUser);
		return "jsp/userList";
	}
	
	@RequestMapping("deleteUser")
	public String deleteUser (@RequestParam("id")int id) {
		userService.deleteUser(id);
		return "redirect:/user/userList?name=a&age=1";
	}
	
}
