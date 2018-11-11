package com.omnitutor.application.services.query;

import com.omnitutor.application.entity.UserEntity;

public interface LoginQueryService {

	UserEntity logIn(String username, String password) throws Exception;
	
}
