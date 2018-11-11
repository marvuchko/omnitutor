package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.CommentEntity;
import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.repository.CommentRepository;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.services.query.CommentQueryService;

@Service
public class CommentQueryServiceImpl implements CommentQueryService {

	private final CommentRepository commentRepository;
	private final CourseRepository courseRepository;
	
	@Override
	public List<CommentEntity> findAllByCourse(Long courseId) throws Exception {
		CourseEntity course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("User not found in the database."));
		return commentRepository.findAllByCourseOrderByIdDesc(course);
	}

	@Autowired
	public CommentQueryServiceImpl(
			CommentRepository commentRepository,
			CourseRepository courseRepository) {
		this.commentRepository = commentRepository;
		this.courseRepository = courseRepository;
	}
	
}
