package com.omnitutor.application.services.command.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.entity.UserOnCourseEntity;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.repository.UserOnCourseRepository;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.command.UserOnCourseCommandService;

@Service
public class UserOnCourseCommandImpl implements UserOnCourseCommandService {

	private final UserOnCourseRepository userOnCourseRepository;

	private final UserRepository userRepository;

	private final CourseRepository courseRepository;

	@Autowired
	public UserOnCourseCommandImpl(UserOnCourseRepository userOnCourseRepository, UserRepository userRepository,
			CourseRepository courseRepository) {
		this.userOnCourseRepository = userOnCourseRepository;
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public boolean addUserOnCourseIfNotExists(Long userId, Long courseId) throws Exception {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User not found in the database"));
		CourseEntity course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("Course not found in the database"));
		if (userOnCourseExists(user, course))
			return false;
		UserOnCourseEntity userOnCourse = new UserOnCourseEntity();
		userOnCourse.setHasQuiz(new Boolean(false));
		userOnCourse.setScore(new Integer(0));
		userOnCourse.setUser(user);
		userOnCourse.setCourse(course);
		userOnCourseRepository.save(userOnCourse);
		return true;
	}

	private boolean userOnCourseExists(UserEntity user, CourseEntity course) {
		return userOnCourseRepository.findAllByUserAndCourse(user, course).isPresent();
	}

}
