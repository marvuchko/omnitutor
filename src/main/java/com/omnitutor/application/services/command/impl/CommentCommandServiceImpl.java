package com.omnitutor.application.services.command.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.CommentEntity;
import com.omnitutor.application.entity.CourseEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.repository.CommentRepository;
import com.omnitutor.application.repository.CourseRepository;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.command.CommentCommandService;

@Service
public class CommentCommandServiceImpl implements CommentCommandService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final CourseRepository courseRepository;
	
	@Override
	public void createComment(Long courseId, Long userId, String text) throws Exception {
		UserEntity user = userId == null ? null : userRepository.findById(userId).get();
		CourseEntity course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("Course not found in the database."));
		CommentEntity comment = new CommentEntity();
		comment.setCourse(course);
		comment.setCreator(user);
		comment.setText(text);
		commentRepository.save(comment);
	}
	
	@Autowired
	public CommentCommandServiceImpl(
			CommentRepository commentRepository,
			UserRepository userRepository,
			CourseRepository courseRepository) {
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
	}

}
