package com.omnitutor.application.services.query;

import java.util.List;

import com.omnitutor.application.entity.QuestionAnswerEntity;

public interface QuestionAnswerQueryService {

	List<QuestionAnswerEntity> findAllByQuestion(Long questionId) throws Exception;
	
}
