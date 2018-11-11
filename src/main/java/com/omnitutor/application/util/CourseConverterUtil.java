package com.omnitutor.application.util;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.entity.CourseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseConverterUtil {

	public static CourseQuery convertEntityToDomain(CourseEntity entity) {
		if(entity == null) return null;
		return CourseQuery.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.detailedDescription(entity.getDetailedDescription())
				.imageUrl(entity.getImageUrl())
				.rating(entity.getRating())
				.creator(UserConverterUtil.convertEntityToDomain(entity.getCreator()))
				.maxPoints(entity.getMaxPoints())
				.build();
	}
	
	public static List<CourseQuery> convertEntityListToDomainList(List<CourseEntity> entities) {
		return entities.stream()
				.map(CourseConverterUtil::convertEntityToDomain).collect(Collectors.toList());
	}
	
}
