package com.omnitutor.application.services.command;

public interface UserOnLectureCommandService {

	void addUserOnLecture(Long userId, Long lectureId) throws Exception;
	
}
