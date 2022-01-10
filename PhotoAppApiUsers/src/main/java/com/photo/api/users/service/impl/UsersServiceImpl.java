package com.photo.api.users.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.photo.api.users.data.UserEntity;
import com.photo.api.users.data.UsersRepository;
import com.photo.api.users.service.UsersService;
import com.photo.api.users.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService{

	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private UsersRepository usersRepository;
	
	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepository=usersRepository;
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	}
	
	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		
		ModelMapper modelMapper=new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		// here userDto is source, UserEntity.class is Destination
		UserEntity userEntity= modelMapper.map(userDto, UserEntity.class);
		
		usersRepository.save(userEntity);
		
		UserDto returnValue=modelMapper.map(userEntity, UserDto.class);
		
		return returnValue;
	}

}
