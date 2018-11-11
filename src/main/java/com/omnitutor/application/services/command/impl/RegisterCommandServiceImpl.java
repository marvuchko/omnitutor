package com.omnitutor.application.services.command.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.RoleEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.repository.RoleRepository;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.command.RegisterCommandService;

@Service
public class RegisterCommandServiceImpl implements RegisterCommandService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	@Override
	public UserEntity createNewUser(UserEntity newUser) throws Exception {
		userRepository.findByUsername(newUser.getUsername())
			.ifPresent(e -> new Exception("User already exists in the database."));
		RoleEntity role = roleRepository.findByName("User")
				.orElseThrow(() -> new Exception("Role not found."));
		newUser.setRole(role);
		return userRepository.save(newUser);
	}
	
	@Autowired
	public RegisterCommandServiceImpl(
			UserRepository userRepository,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

}
