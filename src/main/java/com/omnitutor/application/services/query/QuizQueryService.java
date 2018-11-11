package com.omnitutor.application.services.query;

import java.util.List;

import com.omnitutor.application.entity.QuizEntity;

public interface QuizQueryService {
	
	List<QuizEntity> findAllQuizzesForCourse(Long courseId) throws Exception;

	QuizEntity findById(Long quizId) throws Exception;	

}
