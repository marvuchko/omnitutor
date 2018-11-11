package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.services.query.CourseQueryService;

@Service
public class CourseQueryServiceImpl implements CourseQueryService {

	private final CourseRepository courseRepository;
	
	@Autowired
	public CourseQueryServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}
	
	@Override
	public List<CourseEntity> findAllCourses() {
		return courseRepository.findAll();
	}

	@Override
	public Page<CourseEntity> findAllByTitleContaining(String title, Pageable pageable) {
		return courseRepository.findAllByTitleIgnoreCaseContaining(title, pageable);
	}

	@Override
	public CourseEntity findById(Long id) {
		return courseRepository.findById(id).orElse(null);
	}

	@Override
	public List<CourseEntity> findAllByCreator(UserEntity creator) {
		return courseRepository.findAllByCreator(creator);
	}

	@Override
	public List<CourseEntity> findAllByTitleContainingOrderByIdDesc(String title, Pageable pageable) {
		return courseRepository.findAllByTitleContainingOrderByIdDesc(title, pageable).getContent();
	}

	@Override
	public List<CourseEntity> findAllByTitleContainingOrderByRatingDesc(String title, Pageable pageable) {
		return courseRepository.findAllByTitleContainingOrderByRatingDesc(title, pageable).getContent();
	}

}
