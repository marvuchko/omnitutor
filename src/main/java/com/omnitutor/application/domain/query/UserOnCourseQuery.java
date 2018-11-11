package com.omnitutor.application.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserOnCourseQuery {

	private Long id;
	
	private UserQuery user;
	
	private CourseQuery course;
	
	private Boolean hasQuiz;
	
	private Integer score;
	
}
