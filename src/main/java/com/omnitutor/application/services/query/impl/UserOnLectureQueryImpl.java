package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.entity.UserOnLectureEntity;
import com.omnitutor.application.repository.UserOnLectureRepository;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.query.UserOnLectureQueryService;

@Service
public class UserOnLectureQueryImpl implements UserOnLectureQueryService {

	private final UserRepository userRepository;
	
	private final UserOnLectureRepository userOnLectureRepository;
	
	@Autowired
	public UserOnLectureQueryImpl(
			UserRepository userRepository,
			UserOnLectureRepository userOnLectureRepository) {
		this.userOnLectureRepository = userOnLectureRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public List<UserOnLectureEntity> findAllByUser(Long userId) throws Exception {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User not found in the database"));
		return userOnLectureRepository.findAllByUser(user);
	}

}
