package com.omnitutor.application.domain.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionAnswerCommand {
	
	private String text;
	
	private Boolean isCorrect;
	
}
