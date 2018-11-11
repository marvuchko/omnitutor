package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.QuizEntity;

public interface QuizRepository extends JpaRepository<QuizEntity, Long> {

	List<QuizEntity> findAllByCourse(CourseEntity course);

}
