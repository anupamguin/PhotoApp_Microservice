package com.photo.api.users.data;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photo.api.users.model.AlbumResponseModel;

@FeignClient(name ="ALBUMS-WS")
public interface AlbumsServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> userAlbums(@PathVariable String id);
}
