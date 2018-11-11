package com.omnitutor.application.util;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.query.CommentQuery;
import com.omnitutor.application.entity.CommentEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentConverterUtil {

	public static CommentQuery convertEntityToDomain(CommentEntity entity) {
		return CommentQuery.builder()
				.id(entity.getId())
				.creator(UserConverterUtil.convertEntityToDomain(entity.getCreator()))
				.text(entity.getText())
				.build();
	}
	
	public static List<CommentQuery> convertEntityListToDomainList(List<CommentEntity> entities) {
		if(entities == null) return null;
		return entities.stream()
				.map(CommentConverterUtil::convertEntityToDomain).collect(Collectors.toList());
	}
	
}
