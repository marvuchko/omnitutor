package com.omnitutor.application.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionQuery {
	
	private Long id;
	
	private String questionText;
	
	private QuizQuery quiz;

}
