package com.photo.api.users.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photo.api.users.model.AlbumResponseModel;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(name = "ALBUMS-WS")
public interface AlbumsServiceClient {

	@GetMapping("/users/{id}/albums")
	@CircuitBreaker(name = "albums-ws",fallbackMethod ="userAlbumsFallback")
	public List<AlbumResponseModel> userAlbums(@PathVariable String id);
	
	default List<AlbumResponseModel> userAlbumsFallback(String id, Throwable exception){
		System.err.println("Default Albums print here... ALbums microservices not working");
		System.err.println("Exception took Place: "+exception.getMessage());
		System.err.println("Exception : "+exception);
		return new ArrayList<>();	
	}
}
