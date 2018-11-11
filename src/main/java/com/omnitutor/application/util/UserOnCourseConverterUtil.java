package com.omnitutor.application.util;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.query.UserOnCourseQuery;
import com.omnitutor.application.entity.UserOnCourseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOnCourseConverterUtil {

	public static UserOnCourseQuery convertEntityToDomain(UserOnCourseEntity entity) {
		return UserOnCourseQuery.builder()
				.id(entity.getId())
				.user(UserConverterUtil.convertEntityToDomain(entity.getUser()))
				.course(CourseConverterUtil.convertEntityToDomain(entity.getCourse()))
				.hasQuiz(entity.getHasQuiz())
				.score(entity.getScore())
				.build();
	}
	
	public static List<UserOnCourseQuery> convertEntityListToDomainList(List<UserOnCourseEntity> entities) {
		return entities.stream().map(UserOnCourseConverterUtil::convertEntityToDomain)
				.collect(Collectors.toList());
	}
	
}
