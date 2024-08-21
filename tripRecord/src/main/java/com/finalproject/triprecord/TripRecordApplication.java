package com.finalproject.triprecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
public class TripRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripRecordApplication.class, args);
	}

}
