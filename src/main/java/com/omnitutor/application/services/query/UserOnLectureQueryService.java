package com.omnitutor.application.services.query;

import java.util.List;

import com.omnitutor.application.entity.UserOnLectureEntity;

public interface UserOnLectureQueryService {

	List<UserOnLectureEntity> findAllByUser(Long userId) throws Exception;
	
}
