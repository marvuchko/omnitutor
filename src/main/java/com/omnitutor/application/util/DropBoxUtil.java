package com.omnitutor.application.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;

public class DropBoxUtil {
	
    public static SharedLinkMetadata uploadFile(String relativePath, String extension, ByteArrayOutputStream data) throws UploadErrorException, DbxException, IOException {
    	DbxRequestConfig config = DbxRequestConfig.newBuilder("omnitutor").build();
    	InputStream input = new ByteArrayInputStream(data.toByteArray());
    	DbxClientV2 client = new DbxClientV2(config, RequestUtil.DROPBOX_ACCESS_TOKEN);
    	FileMetadata metaData = client.files().uploadBuilder(relativePath + "/" + System.currentTimeMillis() + "." + extension)
    			.uploadAndFinish(input);
    	return client.sharing().createSharedLinkWithSettings(metaData.getPathLower());
    }
	
}
