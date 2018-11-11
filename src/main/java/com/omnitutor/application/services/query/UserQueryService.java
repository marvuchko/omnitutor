package com.omnitutor.application.services.query;

import com.omnitutor.application.entity.UserEntity;

public interface UserQueryService {

	UserEntity findById(Long userId) throws Exception;

	UserEntity findByEmail(String value) throws Exception;
	
}
