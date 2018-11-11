package com.omnitutor.application.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentQuery {

	private Long id;
	private String text;
	private UserQuery creator;
	
}
