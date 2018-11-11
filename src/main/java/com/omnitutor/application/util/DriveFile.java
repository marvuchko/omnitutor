package com.omnitutor.application.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriveFile {

	private String id;
	private String webContentLink;
	private String embedLink;
	private String title;
	private String mimeType;
	private String thumbnailLink;
	
}
