package com.davidauz.blkm_common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages={"com.davidauz.blkm_common"}) // do not remove this unless you like 'bean not found' error
public class common_configuration {

	private static final Logger logger = LoggerFactory.getLogger(common_configuration.class);
	public static void main(String[] args) { // dummy main class for (stupid) maven
//        SpringApplication.run(bulk_utils_cfg.class, args);
		logger.info("************************* common_configuration main called *************************");
	}
}
