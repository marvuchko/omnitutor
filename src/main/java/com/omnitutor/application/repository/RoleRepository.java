package com.omnitutor.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	Optional<RoleEntity> findByName(String name);
	
}
