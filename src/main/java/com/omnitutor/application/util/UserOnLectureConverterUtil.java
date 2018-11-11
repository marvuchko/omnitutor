package com.omnitutor.application.util;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.query.UserOnLectureQuery;
import com.omnitutor.application.entity.UserOnLectureEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOnLectureConverterUtil {

	public static UserOnLectureQuery convertEntityToDomain(UserOnLectureEntity entity) {
		if(entity == null) return null;
		return UserOnLectureQuery.builder()
				.id(entity.getId())
				.user(UserConverterUtil.convertEntityToDomain(entity.getUser()))
				.lecture(LectureConverterUtil.convertEntityToDomain(entity.getLecture()))
				.build();
	}

	public static List<UserOnLectureQuery> convertEntityListToDomainList(List<UserOnLectureEntity> entities) {
		if(entities == null) return null;
		return entities.stream().map(UserOnLectureConverterUtil::convertEntityToDomain)
				.collect(Collectors.toList());
	} 
	
}
