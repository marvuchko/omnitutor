package com.omnitutor.application.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {
	
	public static final String GRANT_TYPE = "grant_type";
	
	public static final String JWT_BEARER = "urn:ietf:params:oauth:grant-type:jwt-bearer";

	public static final String ASSERTION = "assertion";
	
	public static final String GOOGLE_DRIVE_AUTH = "https://www.googleapis.com/auth/drive";
	
	public static final String GOOGLE_DRIVE_UPLOAD = "https://www.googleapis.com/upload/drive/v2/files?uploadType=media&key=AIzaSyCgwmXesqT3rrqHDfjXRhqQWv-T57hhlGE";
	
	public static final String DROPBOX_FOLDER_PROFILE = "https://www.dropbox.com/sh/hwes144xk5ktbgr/AADAumRwmrCV_5CM6-puqbhwa?dl=0";

	public static final String DROPBOX_ACCESS_TOKEN = "RyMMEohwwcAAAAAAAAAAEpMSZTzQCbhphG8tjLFDxnV93N1UhY8Od8ktKc8DWc6-";
	
}
