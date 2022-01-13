package com.nameniap.mpgtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * MPGTracker Spring Boot Application.
 *
 * @author Stephen Paine
 *
 */
@SpringBootApplication
public class MPGTrackerApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MPGTrackerApplication.class);
	}

    public static void main(String[] args) {
        SpringApplication.run(MPGTrackerApplication.class, args);
    }

}
