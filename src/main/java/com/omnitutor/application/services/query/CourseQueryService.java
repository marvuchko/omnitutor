package com.omnitutor.application.services.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.UserEntity;

public interface CourseQueryService {

	List<CourseEntity> findAllCourses();
	
	CourseEntity findById(Long id);
	
	Page<CourseEntity> findAllByTitleContaining(String title, Pageable pageable);
	
	List<CourseEntity> findAllByCreator(UserEntity creator);
	
	List<CourseEntity> findAllByTitleContainingOrderByIdDesc(String title, Pageable pageable);
	
	List<CourseEntity> findAllByTitleContainingOrderByRatingDesc(String title, Pageable pageable);
	
}
