package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.CommentEntity;
import com.omnitutor.application.entity.CourseEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	List<CommentEntity> findAllByCourseOrderByIdDesc(CourseEntity course);

}
