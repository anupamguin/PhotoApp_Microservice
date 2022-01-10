package com.photo.api.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserModel {

	@NotBlank(message = "First name can't be Empty")
	@Size(min = 2,message = "First name must not be less than 2 Characters")
	private String firstName;
	
	@NotBlank(message = "Last name can't be Empty")
	@Size(min = 2,message = "Last name must not be less than 2 Characters")
	private String lastName;
	
	@NotBlank(message = "Password can't be Empty")
	@NotNull(message = "Password can't be Null")
	@Size(min = 6,max = 20,message = "Password must be greater than 6 & less than 20 Characters")
	private String password;
	
	@NotNull(message = "Email can't be Null")
	@Email
	private String email;

	public UserModel() {
		super();
	}

	public UserModel(String firstName, String lastName, String password, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
