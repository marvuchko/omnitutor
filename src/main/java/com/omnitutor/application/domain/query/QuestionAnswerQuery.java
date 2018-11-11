package com.omnitutor.application.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionAnswerQuery {

	private Long id;
	
	private String text;
	
	private Boolean isCorrect;
	
	private QuestionQuery question;
	
	@Override
	public String toString() {
		return text;
	}
	
}
