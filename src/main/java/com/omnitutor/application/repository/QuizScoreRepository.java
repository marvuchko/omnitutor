package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.QuizScoreEntity;
import com.omnitutor.application.entity.UserOnCourseEntity;

public interface QuizScoreRepository extends JpaRepository<QuizScoreEntity, Long> {

	QuizScoreEntity findByUserOnCourse(UserOnCourseEntity userOnCourse);

	List<QuizScoreEntity> findAllByUserOnCourse(UserOnCourseEntity userOnCourse);

}
