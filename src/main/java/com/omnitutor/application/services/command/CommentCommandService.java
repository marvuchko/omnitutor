package com.omnitutor.application.services.command;

public interface CommentCommandService {
	
	void createComment(Long courseId, Long userId, String text) throws Exception;
	
}
