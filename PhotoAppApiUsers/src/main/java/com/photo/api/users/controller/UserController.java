package com.photo.api.users.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photo.api.users.model.UserModel;
import com.photo.api.users.service.UsersService;
import com.photo.api.users.shared.UserDto;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private Environment env;
	
	@Autowired
	private UsersService usersService;
	
	@GetMapping("/status")
	public String status() { // to access eureka will assign a random port on this port we can access
		return "Working on port: "+env.getProperty("local.server.port");
	}
	
	@PostMapping(value = "/add",
			consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
	 		produces ={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> createUser(@Valid @RequestBody UserModel model,BindingResult result) {

//Validation purpose
		if(result.hasErrors()) {
			Map<String,String> errorMap=new HashMap<String,String>();
			for(FieldError error:result.getFieldErrors()) {
				errorMap.put(error.getField(),error.getDefaultMessage());
			}
			
			return new ResponseEntity<Map<String,String>>(errorMap,HttpStatus.BAD_REQUEST); 
		}
		
		ModelMapper modelMapper=new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto=modelMapper.map(model, UserDto.class);
		System.out.println(userDto);
		UserDto returnValue= usersService.createUser(userDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}
}
