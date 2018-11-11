package com.omnitutor.application.domain.command;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CourseCommand {

	private String title;
	private String imageUrl;
	private String description;
	private Double rating;
	private Date createdAt;
	private String detailedDescription;
	private UserCommand creator;
	private Integer maxPoints;
	private List<LectureCommand> lectures;
	
}
