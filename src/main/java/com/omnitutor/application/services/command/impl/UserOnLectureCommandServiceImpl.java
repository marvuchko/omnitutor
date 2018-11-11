package com.omnitutor.application.services.command.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnitutor.application.entity.LectureEntity;
import com.omnitutor.application.entity.UserEntity;
import com.omnitutor.application.entity.UserOnLectureEntity;
import com.omnitutor.application.repository.LectureRepository;
import com.omnitutor.application.repository.UserOnLectureRepository;
import com.omnitutor.application.repository.UserRepository;
import com.omnitutor.application.services.command.UserOnLectureCommandService;

@Service
public class UserOnLectureCommandServiceImpl implements UserOnLectureCommandService {

	private final UserRepository userRepository;
	
	private final LectureRepository lectureRepository;
	
	private final UserOnLectureRepository userOnLectureRepository;
	
	@Autowired
	public UserOnLectureCommandServiceImpl(
			UserRepository userRepository,
			LectureRepository lectureRepository,
			UserOnLectureRepository userOnLectureRepository) {
		this.userRepository = userRepository;
		this.lectureRepository = lectureRepository;
		this.userOnLectureRepository = userOnLectureRepository;
	}
	
	@Override
	public void addUserOnLecture(Long userId, Long lectureId) throws Exception {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User not found in the database"));
		LectureEntity lecture = lectureRepository.findById(lectureId)
				.orElseThrow(() -> new Exception("Lecture not found in the database"));
		if(userHasCompleatedLecture(user, lecture)) return;
		UserOnLectureEntity userOnLecture = new UserOnLectureEntity();
		userOnLecture.setUser(user);
		userOnLecture.setLecture(lecture);
		userOnLectureRepository.save(userOnLecture);
	}

	private boolean userHasCompleatedLecture(UserEntity user, LectureEntity lecture) {
		return userOnLectureRepository.findByUserAndLecture(user, lecture) != null;
	}

}
