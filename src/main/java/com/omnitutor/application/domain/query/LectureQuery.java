package com.omnitutor.application.domain.query;

import com.omnitutor.application.domain.LectureDomain;

import lombok.Getter;

@Getter
public class LectureQuery extends LectureDomain {

	private Long id;
	private String videoURL;
	private String imageURL;
	
	public LectureQuery(
			Long id,
			String videoURL,
			String imageURL,
			String description,
			Integer durationInMinutes,
			String title) {
		this.id = id;
		this.videoURL = videoURL;
		this.imageURL = imageURL;
		this.description = description;
		this.durationInMinutes = durationInMinutes;
		this.title = title;
	}
	
}
