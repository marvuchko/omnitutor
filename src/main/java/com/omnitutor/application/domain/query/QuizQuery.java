package com.omnitutor.application.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizQuery {

	private Long id;
	
	private String name;
	
	private String description;
	
	private Integer maxPoints;
	
	private CourseQuery course;
	
}
