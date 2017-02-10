package com.vertrualNoriceBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.vertrualNoriceBoard.config.PicturesUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({PicturesUploadProperties.class})
public class NoticeBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoticeBoardApplication.class, args);
	}
}
