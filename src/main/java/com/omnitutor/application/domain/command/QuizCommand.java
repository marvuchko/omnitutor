package com.omnitutor.application.domain.command;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class QuizCommand {

	private Long id;
	
	private String name;
	
	private String description;
	
	private Long courseId;
	
	private List<QuestionCommand> questions;
	
}
