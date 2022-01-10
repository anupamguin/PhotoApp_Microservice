package com.photo.api.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private Environment env;
	
	@GetMapping("/status")
	public String status() { // to access eureka will assign a random port on this port we can access
		return "Working on port: "+env.getProperty("local.server.port");
	}
	
	@GetMapping("/age")
	public String age() {
		return "My age is 22";
	}
}
