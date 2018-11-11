package com.omnitutor.application.services.query;

import java.util.List;

import com.omnitutor.application.entity.LectureEntity;

public interface LectureQueryService {
	
	List<LectureEntity> findAllByCourse(Long courseId) throws Exception;

	LectureEntity findById(Long lectureId) throws Exception;	

}
