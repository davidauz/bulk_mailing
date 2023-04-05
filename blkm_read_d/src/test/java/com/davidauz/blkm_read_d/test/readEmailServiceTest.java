package com.davidauz.blkm_read_d.test;

import com.davidauz.blkm_read_d.service.readEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;


@SpringBootApplication
@SpringBootTest
@ContextConfiguration
@ComponentScan(basePackages={"com.davidauz.blkm_common","package com.davidauz.blkm_read_d"})
@EntityScan(basePackages={"com.davidauz.blkm_common.*"})
@DependsOn("mailMessageRepository")
//@EnableJpaRepositories(basePackages = "com.davidauz.blkm_common.*")
class readEmailServiceTest {
	@Autowired  private readEmailService res;

	@Test
	void read_messages_in_inbox() {
res.read_messages_in_inbox();
	}
}