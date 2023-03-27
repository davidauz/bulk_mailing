package com.davidauz.bulk_mailing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BulkMailing // extends SpringBootServletInitializer
 {

	public static void main(String[] args) {
		SpringApplication.run(BulkMailing.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(BulkMailing.class);
//	}
}
