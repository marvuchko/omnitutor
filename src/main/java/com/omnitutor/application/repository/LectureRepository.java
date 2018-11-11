package com.omnitutor.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.LectureEntity;

public interface LectureRepository extends JpaRepository<LectureEntity, Long> {

	List<LectureEntity> findAllByCourse(CourseEntity entity);

}