package com.omnitutor.application.services.query;

import java.util.List;

import com.omnitutor.application.entity.QuestionEntity;

public interface QuestionQueryService {

	List<QuestionEntity> findAllByQuiz(Long quizId) throws Exception;
	
}
