package com.omnitutor.application.services.query;

import java.util.List;

import com.omnitutor.application.entity.CommentEntity;

public interface CommentQueryService {

	List<CommentEntity> findAllByCourse(Long courseId) throws Exception;
	
}
