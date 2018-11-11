package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.UserEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
	
	Page<CourseEntity> findAllByTitleIgnoreCaseContaining(String title, Pageable pageable);

	List<CourseEntity> findAllByCreator(UserEntity creator);

	Page<CourseEntity> findAllByTitleContainingOrderByIdDesc(String title, Pageable pageable);

	Page<CourseEntity> findAllByTitleContainingOrderByRatingDesc(String title, Pageable pageable);
	
}
