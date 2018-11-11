package com.omnitutor.application.domain.query;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseQuery {

	private Long id;
	private String title;
	private String imageUrl;
	private String description;
	private Double rating;
	private Date createdAt;
	private String detailedDescription;
	private UserQuery creator;
	private Integer maxPoints;
	
	@Override
	public String toString() {
		return title;
	}
	
}
