package com.omnitutor.application.util;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.query.QuestionQuery;
import com.omnitutor.application.entity.QuestionEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionConverterUtil {
	
	public static QuestionQuery convertEntityToDomain(QuestionEntity entity) {
		if(entity == null) return null;
		return QuestionQuery.builder()
				.id(entity.getId())
				.questionText(entity.getQuestionText())
				.quiz(QuizConverterUtil.convertEntityToDomain(entity.getQuiz()))
				.build();
	}
	
	public static List<QuestionQuery> convertEntityListToDomainList(List<QuestionEntity> entities) {
		if(entities == null) return null;
		return entities.stream()
				.map(QuestionConverterUtil::convertEntityToDomain).collect(Collectors.toList());
	}

}
