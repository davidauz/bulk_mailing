package com.davidauz.bulk_mailing.common_classes.repo;

import com.davidauz.bulk_mailing.common_classes.entity.blk_MailMessage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;


@SpringBootApplication(scanBasePackages = "com.davidauz.bulk_mailing.common_classes.*")
@SpringBootTest
@ContextConfiguration
@ComponentScan(basePackages={"com.davidauz.bulk_mailing.common_classes.*"})
@EntityScan(basePackages={"com.davidauz.bulk_mailing.common_classes.*"})
@DependsOn("mailMessageRepository")
@EnableJpaRepositories(basePackages = "com.davidauz.bulk_mailing.common_classes.*")
class MailMessageRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(MailMessageRepositoryTest.class);
    @Autowired
    private MailMessageRepository mailMessageRepo;

    @Test
    public void test_min_id() {
        long smallest_id = mailMessageRepo.getMinId(blk_MailMessage.SENT_STATUS.ENQUEUED);
        logger.info("Smallest enqueued ID : " + smallest_id);
    }

    @Configuration
    static class Config {
    }
}