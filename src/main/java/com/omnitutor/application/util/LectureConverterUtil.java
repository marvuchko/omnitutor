package com.omnitutor.application.util;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.LectureDomain;
import com.omnitutor.application.domain.command.LectureCommand;
import com.omnitutor.application.domain.query.LectureQuery;
import com.omnitutor.application.entity.LectureEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LectureConverterUtil {

	public static LectureQuery convertEntityToDomain(LectureEntity entity) {
		if(entity == null) return null;
		return new LectureQuery(
				entity.getId(),
				entity.getVideoURL(),
				entity.getImageURL(),
				entity.getDescription(),
				entity.getDurationInMinutes(),
				entity.getTitle());
	}
	
	public static LectureDomain convertQueryToViewModel(LectureQuery query) {
		if(query == null) return null;
		return (LectureDomain) query;
	}
	
	public static LectureDomain convertCommandToViewModel(LectureCommand command) {
		if(command == null) return null;
		return (LectureDomain) command;
	}
	
	public static List<LectureQuery> convertEntityListToDomainList(List<LectureEntity> entities) {
		if(entities == null) return null;
		return entities.stream()
				.map(LectureConverterUtil::convertEntityToDomain).collect(Collectors.toList());
	}
	
}
