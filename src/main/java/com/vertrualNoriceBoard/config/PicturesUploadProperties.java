package com.vertrualNoriceBoard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "upload.pictures")
public class PicturesUploadProperties {

	private Resource uploadPath;

	public Resource getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = new DefaultResourceLoader().getResource(uploadPath);
	}

}
