package com.omnitutor.application.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class LectureDomain {

	protected String description;
	protected Integer durationInMinutes;
	protected String title;
	
}
