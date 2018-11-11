package com.omnitutor.application.util;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.command.QuizCommand;
import com.omnitutor.application.domain.query.QuizQuery;
import com.omnitutor.application.entity.QuizEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizConverterUtil {

	public QuizEntity domainToEntity(QuizCommand command) {
		if(command == null) return null;
		QuizEntity entity = new QuizEntity();
		entity.setDescription(command.getDescription());
		entity.setName(command.getName());
		return entity;
	}
	
	public static QuizQuery convertEntityToDomain(QuizEntity entity) {
		if(entity == null) return null;
		return QuizQuery.builder()
				.id(entity.getId())
				.course(CourseConverterUtil.convertEntityToDomain(entity.getCourse()))
				.description(entity.getDescription())
				.maxPoints(entity.getMaxPoints())
				.name(entity.getName())
				.build();
	}

	public static List<QuizQuery> convertEntityListToDomainList(List<QuizEntity> entities) {
		if(entities == null) return null;
		return entities.stream()
				.map(QuizConverterUtil::convertEntityToDomain).collect(Collectors.toList());
	}
	
}
