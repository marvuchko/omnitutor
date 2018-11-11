package com.omnitutor.application.util;

import com.omnitutor.application.domain.command.UserCommand;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.entity.UserEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverterUtil {
	
	public static UserQuery convertEntityToDomain(UserEntity entity) {
		if(entity == null) return null;
		return UserQuery.builder()
				.id(entity.getId())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.email(entity.getEmail())
				.imageURL(entity.getImageURL())
				.username(entity.getUsername())
				.password(entity.getPassword())
				.role(entity.getRole().getName())
				.firstLogin(entity.getFirstLogin())
				.lastLogin(entity.getLastLogin())
				.build();
	}

	public static UserEntity convertDomainToEntity(UserCommand user) {
		UserEntity entity = new UserEntity();
		entity.setId(user.getId());
		entity.setEmail(user.getEmail());
		entity.setFirstName(user.getFirstName());
		entity.setImageURL(user.getImageURL());
		entity.setUsername(user.getUsername());
		entity.setPassword(user.getPassword());
		entity.setLastName(user.getLastName());
		return entity;
	}

	public static UserQuery convertDomainToDomain(UserCommand modifiedUser) {
		if(modifiedUser == null) return null;
		return UserQuery.builder()
				.id(modifiedUser.getId())
				.firstName(modifiedUser.getFirstName())
				.lastName(modifiedUser.getLastName())
				.email(modifiedUser.getEmail())
				.imageURL(modifiedUser.getImageURL())
				.username(modifiedUser.getUsername())
				.password(modifiedUser.getPassword())
				.role(modifiedUser.getRole())
				.firstLogin(modifiedUser.getFirstLogin())
				.lastLogin(modifiedUser.getLastLogin())
				.build();
	}

}
