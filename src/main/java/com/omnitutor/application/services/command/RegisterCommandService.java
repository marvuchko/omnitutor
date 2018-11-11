package com.omnitutor.application.services.command;

import com.omnitutor.application.entity.UserEntity;

public interface RegisterCommandService {

	UserEntity createNewUser(UserEntity newUser) throws Exception;
	
}
