package com.omnitutor.application.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.query.QuestionAnswerQuery;
import com.omnitutor.application.entity.QuestionAnswerEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionAnswerConverterUtil {

	public static QuestionAnswerQuery convertEntityToDomain(QuestionAnswerEntity entity) {
		if (entity == null)
			return null;
		return QuestionAnswerQuery.builder().id(entity.getId()).isCorrect(entity.getIsCorrect())
				.question(QuestionConverterUtil.convertEntityToDomain(entity.getQuestion())).text(entity.getText())
				.build();
	}

	public static ArrayList<QuestionAnswerQuery> convertEntityListToDomainList(List<QuestionAnswerEntity> entities) {
		if (entities == null)
			return null;
		return (ArrayList<QuestionAnswerQuery>) entities.stream()
				.map(QuestionAnswerConverterUtil::convertEntityToDomain).collect(Collectors.toList());
	}

}
