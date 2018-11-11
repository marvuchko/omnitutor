package com.omnitutor.application.domain.command;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class QuestionCommand {

	private Long id;
	
	private String questionText;
	
	private List<QuestionAnswerCommand> answers;
	
}
