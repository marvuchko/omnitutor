package com.omnitutor.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.entity.UserOnCourseEntity;

public interface UserOnCourseRepository extends JpaRepository<UserOnCourseEntity, Long> {
	
	List<UserOnCourseEntity> findAllByCourseId(Long courseId);

	List<UserOnCourseEntity> findAllByUserId(Long userId);

	Optional<UserOnCourseEntity> findAllByUserAndCourse(UserEntity user, CourseEntity course);

}
