package com.omnitutor.application.services.command;

public interface UserOnCourseCommandService {
	
	boolean addUserOnCourseIfNotExists(Long userId, Long courseId) throws Exception;

}
