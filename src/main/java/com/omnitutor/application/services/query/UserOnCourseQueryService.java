package com.omnitutor.application.services.query;

import java.util.List;

import com.omnitutor.application.entity.UserOnCourseEntity;

public interface UserOnCourseQueryService {

	List<UserOnCourseEntity> getAllCoursesUserHasTaken(Long userId);
	
}
