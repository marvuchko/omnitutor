package com.omnitutor.application.services.command;

import com.omnitutor.application.entity.UserEntity;

public interface UserCommandService {

	void modifyUser(UserEntity modifiedUser) throws Exception;
	
}
