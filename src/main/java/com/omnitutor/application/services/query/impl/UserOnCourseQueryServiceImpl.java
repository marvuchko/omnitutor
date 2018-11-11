package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.UserOnCourseEntity;
import com.omnitutor.application.repository.UserOnCourseRepository;
import com.omnitutor.application.services.query.UserOnCourseQueryService;

@Service
public class UserOnCourseQueryServiceImpl implements UserOnCourseQueryService {

	private final UserOnCourseRepository userOnCourseRepository;
	
	@Autowired
	public UserOnCourseQueryServiceImpl(UserOnCourseRepository userOnCourseRepository) {
		this.userOnCourseRepository = userOnCourseRepository;
	}
	
	public List<UserOnCourseEntity> getAllCoursesUserHasTaken(Long userId) {
		return userOnCourseRepository.findAllByUserId(userId);
	}
	
}
