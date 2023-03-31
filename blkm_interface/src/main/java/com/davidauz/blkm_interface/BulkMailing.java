package com.davidauz.blkm_interface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"com.davidauz.blkm_common", "com.davidauz.blkm_interface"})
//                                             |                              |
//                                             |                              +---without this, no custom login
//                                             +---without this, error missing com.davidauz.blkm_common.entity.blk_MailQueue
@EntityScan(basePackages={"com.davidauz.blkm_interface"})
//                                 |
//                                 +--- without this: Not a managed type: class com.davidauz.blkm_interface.entity.User
@EnableJpaRepositories(basePackages = { "com.davidauz.blkm_common", "com.davidauz.blkm_interface" })
//                                              |                                |
//                                              |                                +---- without this: 'com.davidauz.blkm_interface.repository.UserRepository' could not be found.
//                                              +---- without this: 'com.davidauz.blkm_common.repo.ConfigurationRepository' that could not be found.
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
