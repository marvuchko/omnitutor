package com.omnitutor.application.services.command.impl;

import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.command.UserCommandService;

@Service
public class UserCommandServiceImpl implements UserCommandService {

	private final UserRepository userRepository;
	
	public UserCommandServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public void modifyUser(UserEntity modifiedUser) throws Exception {
		UserEntity user = userRepository.findById(modifiedUser.getId())
				.orElseThrow(() -> new Exception("User not found in database!"));
		if(!user.getFirstName().equals(modifiedUser.getFirstName()))
			user.setFirstName(modifiedUser.getFirstName());
		if(!user.getLastName().equals(modifiedUser.getLastName()))
			user.setLastName(modifiedUser.getLastName());
		if(!user.getUsername().equals(modifiedUser.getUsername()))
			user.setUsername(modifiedUser.getUsername());
		if(!user.getPassword().equals(modifiedUser.getPassword()))
			user.setPassword(modifiedUser.getPassword());
		if(!user.getEmail().equals(modifiedUser.getEmail()))
			user.setEmail(modifiedUser.getEmail());
		if(!user.getImageURL().equals(modifiedUser.getImageURL()))
			user.setImageURL(modifiedUser.getImageURL());
		userRepository.save(user);
	}

}
