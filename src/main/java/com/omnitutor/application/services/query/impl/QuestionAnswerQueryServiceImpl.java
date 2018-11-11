package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.QuestionAnswerEntity;
import com.omnitutor.application.entity.QuestionEntity;
import com.omnitutor.application.repository.QuestionAnswerRepository;
import com.omnitutor.application.repository.QuestionRepository;
import com.omnitutor.application.services.query.QuestionAnswerQueryService;

@Service
public class QuestionAnswerQueryServiceImpl implements QuestionAnswerQueryService {

	private final QuestionAnswerRepository questionAnswerRepository;

	private final QuestionRepository questionRepository;

	@Autowired
	public QuestionAnswerQueryServiceImpl(QuestionAnswerRepository questionAnswerRepository,
			QuestionRepository questionRepository) {
		this.questionAnswerRepository = questionAnswerRepository;
		this.questionRepository = questionRepository;
	}

	@Override
	public List<QuestionAnswerEntity> findAllByQuestion(Long questionId) throws Exception {
		QuestionEntity question = questionRepository.findById(questionId)
				.orElseThrow(() -> new Exception("Question not found in the database!"));
		return questionAnswerRepository.findAllByQuestion(question);
	}

}
