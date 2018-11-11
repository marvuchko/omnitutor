package com.omnitutor.application.services.query.impl;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.query.LoginQueryService;

@Service
public class LoginQueryServiceImpl implements LoginQueryService {

	private final UserRepository userRepository;
	
	@Autowired
	public LoginQueryServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserEntity logIn(String username, String password) throws Exception {
		UserEntity user = userRepository.findByUsernameAndPassword(username, password)
				.orElseThrow(() -> new Exception("User not found in the database."));
		if(user.getFirstLogin() == null) user.setFirstLogin(new Date(System.currentTimeMillis()));
		user.setLastLogin(new Date(System.currentTimeMillis()));
		userRepository.save(user);
		return user;
	}

}
