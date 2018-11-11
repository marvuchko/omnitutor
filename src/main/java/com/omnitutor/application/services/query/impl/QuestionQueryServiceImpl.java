package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.QuestionEntity;
import com.omnitutor.application.entity.QuizEntity;
import com.omnitutor.application.repository.QuestionRepository;
import com.omnitutor.application.repository.QuizRepository;
import com.omnitutor.application.services.query.QuestionQueryService;

@Service
public class QuestionQueryServiceImpl implements QuestionQueryService {

	private final QuestionRepository questionRepository;
	
	private final QuizRepository quizRepository;
	
	@Autowired
	public QuestionQueryServiceImpl(QuestionRepository questionRepository, QuizRepository quizRepository) {
		this.questionRepository = questionRepository;
		this.quizRepository = quizRepository;
	}
	
	@Override
	public List<QuestionEntity> findAllByQuiz(Long quizId) throws Exception {
		QuizEntity quiz = quizRepository.findById(quizId)
				.orElseThrow(() -> new Exception("Quiz not found in the database!"));
		return questionRepository.findAllByQuiz(quiz);
	}

}
