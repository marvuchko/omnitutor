package com.omnitutor.application.services.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.QuizEntity;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.repository.QuizRepository;
import com.omnitutor.application.services.query.QuizQueryService;

@Service
public class QuizQueryServiceImpl implements QuizQueryService {

	private final QuizRepository quizRepository;
	
	private final CourseRepository courseRepository;
	
	@Autowired
	public QuizQueryServiceImpl(QuizRepository quizRepository, CourseRepository courseRepository) {
		this.quizRepository = quizRepository;
		this.courseRepository = courseRepository;
	}
	
	@Override
	public List<QuizEntity> findAllQuizzesForCourse(Long courseId) throws Exception {
		CourseEntity course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("Course not found in the database!"));
		return quizRepository.findAllByCourse(course);
	}

	@Override
	public QuizEntity findById(Long quizId) throws Exception {
		return quizRepository.findById(quizId)
				.orElseThrow(() -> new Exception("Quiz not found in the database!"));
	}

}
