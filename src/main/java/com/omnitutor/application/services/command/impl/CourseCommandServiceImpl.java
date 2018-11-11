package com.omnitutor.application.services.command.impl;

import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.services.command.CourseCommandService;

@Service
public class CourseCommandServiceImpl implements CourseCommandService {

	private final CourseRepository courseRepository;
	
	public CourseCommandServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	} 
	
	@Override
	public void rateCourse(Long courseId, int rating) throws Exception {
		CourseEntity course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("Course not found in the database"));
		course.setRating((course.getRating() + rating) / 2);
		courseRepository.save(course);
	}

}
