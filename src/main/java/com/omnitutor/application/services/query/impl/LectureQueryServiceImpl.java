package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.LectureEntity;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.repository.LectureRepository;
import com.omnitutor.application.services.query.LectureQueryService;

@Service
public class LectureQueryServiceImpl implements LectureQueryService {

	private final LectureRepository lectureRepository;
	private final CourseRepository courseRepository;
	
	@Override
	public List<LectureEntity> findAllByCourse(Long courseId) throws Exception {
		CourseEntity course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("Course not found in database."));
		return lectureRepository.findAllByCourse(course);
	}
	
	@Autowired
	public LectureQueryServiceImpl(
			LectureRepository lectureRepository,
			CourseRepository courseRepository) {
		this.lectureRepository = lectureRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public LectureEntity findById(Long lectureId) throws Exception {
		return lectureRepository.findById(lectureId)
				.orElseThrow(() -> new Exception("Lecture not found in database."));
	}

}
