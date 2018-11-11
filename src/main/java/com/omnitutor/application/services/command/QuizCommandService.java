package com.omnitutor.application.services.command;

import com.omnitutor.application.domain.command.QuizCommand;
import com.omnitutor.application.entity.QuizEntity;

public interface QuizCommandService {

	QuizEntity createQuiz(QuizCommand quiz) throws Exception;
	
	void addPointsToCourse(Long userId, Long courseId, Long quizId, Integer score) throws Exception;
	
}
