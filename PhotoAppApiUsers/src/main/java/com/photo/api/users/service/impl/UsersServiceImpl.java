package com.photo.api.users.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.photo.api.users.data.AlbumsServiceClient;
import com.photo.api.users.data.UserEntity;
import com.photo.api.users.data.UsersRepository;
import com.photo.api.users.model.AlbumResponseModel;
import com.photo.api.users.service.UsersService;
import com.photo.api.users.shared.UserDto;

import feign.FeignException;

@Service
public class UsersServiceImpl implements UsersService {

	@Value("${albums.url}")
	String ALBUM_URL;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private UsersRepository usersRepository;

	private RestTemplate restTemplate;
	
	private AlbumsServiceClient albumsServiceClient;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
			RestTemplate restTemplate,AlbumsServiceClient albumsServiceClient) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.restTemplate = restTemplate;
		this.albumsServiceClient=albumsServiceClient;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		// here userDto is source, UserEntity.class is Destination
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

		usersRepository.save(userEntity);

		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = usersRepository.findByEmail(username);

		if (userEntity == null)
			throw new UsernameNotFoundException("Username not found");

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = usersRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException("Username not found");

		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = usersRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UsernameNotFoundException("User not Found");

		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

/* use RestTemplate to Call Albums microservices */

//		ALBUM_URL = String.format(ALBUM_URL,userId);
//		System.out.println("Album URL: "+ALBUM_URL);
//
//		ResponseEntity<List<AlbumResponseModel>> albumListResponse = null;
//		try {
//			albumListResponse = restTemplate.exchange(ALBUM_URL, HttpMethod.GET, null,
//					new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//					});
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		List<AlbumResponseModel> albumsList = albumListResponse.getBody();

		
/* use Feign client to Call Albums microservices  */
		
		List<AlbumResponseModel> albumsList = null; 
		try {
			albumsList =albumsServiceClient.userAlbums(userId);
		} catch (FeignException e) {
			System.err.println(e.getMessage());
		}
		
		
		userDto.setAlbums(albumsList);

		return userDto;
	}

}
