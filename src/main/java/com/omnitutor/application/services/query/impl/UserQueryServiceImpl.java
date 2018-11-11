package com.omnitutor.application.services.query.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.query.UserQueryService;

@Service
public class UserQueryServiceImpl implements UserQueryService {

	private final UserRepository userRepository;
	
	@Autowired
	public UserQueryServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserEntity findById(Long userId) throws Exception {
		return userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User not found in the database!"));
	}

	@Override
	public UserEntity findByEmail(String value) throws Exception {
		return userRepository.findByEmail(value)
				.orElseThrow(() -> new Exception("User not found in the database!"));
	}

}
