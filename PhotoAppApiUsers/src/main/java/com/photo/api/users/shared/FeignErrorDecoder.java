package com.photo.api.users.shared;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
// here methodKey is the Feign client Interface name on which error Occur
		switch (response.status()) {
		case 400:
			// Do SOmething code here
			break;
		case 404:
			if (methodKey.contains("userAlbums")) {
				System.err.println("Error Feign");
				return new ResponseStatusException(HttpStatus.valueOf(response.status()),
						"Anupam ERROR: " + response.reason());
			}
			break;
		default:
			return new Exception(response.reason());
		}
		return null;
	}

}
