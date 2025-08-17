package com.tweetapp.tweetappbackend;

import com.tweetapp.tweetappbackend.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
@EnableSwagger2
public class TweetappBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(com.tweetapp.tweetappbackend.TweetappBackendApplication.class, args);
	}

}
