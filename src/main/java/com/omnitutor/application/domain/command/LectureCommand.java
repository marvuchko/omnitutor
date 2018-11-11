package com.omnitutor.application.domain.command;

import java.io.ByteArrayOutputStream;

import com.omnitutor.application.domain.LectureDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureCommand extends LectureDomain {

	private ByteArrayOutputStream videoData;
	
	public LectureCommand(
			String description,
			Integer durationInMinutes,
			String title,
			ByteArrayOutputStream videoData) {
		this.title = title;
		this.description = description;
		this.durationInMinutes = durationInMinutes;
		this.videoData = videoData;
	}
	
}
