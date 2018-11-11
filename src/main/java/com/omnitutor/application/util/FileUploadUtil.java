package com.omnitutor.application.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Upload.Receiver;

import lombok.Getter;

@Getter
@Service
@UIScope
public class FileUploadUtil implements Receiver {
	
	private static final long serialVersionUID = 1L;

	public static final String IMAGE = "image";
	
	public static final String VIDEO = "video";
	
	private ByteArrayOutputStream data;
	private String mimeType;
	private String fileType;
	private String fileName;
	private String extension;

    public OutputStream receiveUpload(String filename, String mimeType) {
    	fileType = getType(mimeType);
    	this.mimeType = mimeType; 
    	fileName = filename;
    	extension = filename.split("[.]")[1];
		data = new ByteArrayOutputStream();
		return data;
    }

	private String getType(String mimeType) {
		return mimeType == null ? null : mimeType.split("/")[0];
	}

}
