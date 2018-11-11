package com.omnitutor.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	Optional<UserEntity> findByUsernameAndPassword(String username, String password);

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByEmail(String value);
	
}
