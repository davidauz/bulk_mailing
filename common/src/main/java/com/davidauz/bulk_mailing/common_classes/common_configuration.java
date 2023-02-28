package com.davidauz.bulk_mailing.common_classes;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EntityScan(basePackages={"com.davidauz.bulk_mailing"}) // do not remove this unless you like 'bean not found' error
public class common_configuration {
}
