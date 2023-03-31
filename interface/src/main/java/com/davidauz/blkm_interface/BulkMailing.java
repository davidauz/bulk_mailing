package com.davidauz.blkm_interface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"com.davidauz.blkm_common"})
@EntityScan(basePackages={"com.davidauz.blkm_common"})
@EnableJpaRepositories(basePackages = "com.davidauz.blkm_common")
public class BulkMailing extends SpringBootServletInitializer {
// SpringBootServletInitializer is needed for running in Tomcat

	public static void main(String[] args) {
		SpringApplication.run(BulkMailing.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
// also for running in Tomcat
		return builder.sources(BulkMailing.class);
	}
}
