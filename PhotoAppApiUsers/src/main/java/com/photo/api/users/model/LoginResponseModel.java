package com.photo.api.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseModel {

	@JsonProperty(value = "SUCCESS")
	private boolean success;
	
	@JsonProperty(value = "TOKEN")
	private String token;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LoginResponseModel(boolean success, String token) {
		super();
		this.success = success;
		this.token = token;
	}

}
