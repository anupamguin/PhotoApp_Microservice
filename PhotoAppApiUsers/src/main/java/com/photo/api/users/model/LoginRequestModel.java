package com.photo.api.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequestModel {

	@Email
	@NotBlank(message = "Email not be Blank")
	private String email;
	
	@NotBlank(message = "Password not be Blank")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
