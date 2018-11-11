package com.omnitutor.application.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserOnLectureQuery {
	
	private Long id;
	
	private UserQuery user;
	
	private LectureQuery lecture;
	
}
