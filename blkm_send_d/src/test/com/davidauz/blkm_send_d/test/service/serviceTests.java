package com.davidauz.blkm_send_d.test.service;

import com.davidauz.blkm_common.entity.EmailStatusConstants;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_send_d.sendEmailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

//Description: Tests the business logic in service classes.
//Key Annotations: @RunWith(SpringRunner.class), @SpringBootTest

@SpringBootTest
@Slf4j
public class serviceTests {


	// Create a mock of Resource to change its behaviour for testing
	@MockBean
	private MailMessageRepository mail_msg_repo;

	// Testing instance, mocked `resource` should be injected here
	@InjectMocks
	@Resource
	private sendEmailService sendEmailSrvc;


	@Test
	void test_sending_non_existent_mail_id() {
		given(this.mail_msg_repo.getMinId(EmailStatusConstants.ENQUEUED)).willReturn(-1L);
		given(this.mail_msg_repo.countBySentStatus(EmailStatusConstants.ENQUEUED)).willReturn(5L);

		sendEmailSrvc = new sendEmailService();
		try {
//give a fake mail Id
			sendEmailSrvc.send_messages_in_queue(); // will call sendOneEmail throwing an error
			fail("This is not supposed to happen");
		} catch (Exception e) {
			log.info("Exception thrown as expected");
		}

	}
}

