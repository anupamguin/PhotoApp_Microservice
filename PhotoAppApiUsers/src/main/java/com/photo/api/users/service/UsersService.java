package com.photo.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.photo.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService{
	UserDto createUser(UserDto userdto);
	
	UserDto getUserDetailsByEmail(String email);
}
