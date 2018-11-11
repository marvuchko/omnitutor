package com.omnitutor.application.services.command;

public interface CourseCommandService {

	void rateCourse(Long courseId, int rating) throws Exception;
	
}
