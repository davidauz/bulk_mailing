package com.davidauz.blkm_send_d.test.integration;



import com.davidauz.blkm_common.entity.EmailStatusConstants;
import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.entity.blk_MailQueue;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Integration tests check if the different units, modules or components do work together as a combined entity.
//Here real operations are performed to check the various layers (controller, service, repository).
@SpringBootTest
public class integrationTests {
	@Autowired
	private MailMessageRepository mailMessageRepo;
	@Autowired
	private blk_MailQueue mailQ;

	@Test
		// from Junit
	void test_enqueue_a_message() {
		Long messageId=-1L;
		try {
//See how many messages are queued
			Long lEnqueuedMessages = mailMessageRepo.countBySentStatus(EmailStatusConstants.ENQUEUED);
//Try enqueueing one dummy message
			blk_MailMessage blkmm = new blk_MailMessage();
			blkmm.setSubject("TEST subject");
			blkmm.setBody("TEST content");
			blkmm.setRecipient("someone@somewhere.net");
			blkmm.setIdRecipient(1L);
			blkmm.setIdCampaign(1L);
			mailQ.enqueue(blkmm);
			messageId = blkmm.getId();
//check how many messages there are now
			Long lEnqueuedMessages2 = mailMessageRepo.countBySentStatus(EmailStatusConstants.ENQUEUED);
//must be 1 + the previous value
			assertEquals(1 + lEnqueuedMessages, lEnqueuedMessages2);
//now delete the dummy message from the queue
			mailMessageRepo.deleteById(messageId);
//check how long is the queue now
			lEnqueuedMessages2 = mailMessageRepo.countBySentStatus(EmailStatusConstants.ENQUEUED);
//queue size must be same as the first value
			assertEquals(lEnqueuedMessages, lEnqueuedMessages2);
		}catch (Exception e){
			throw e;
		}finally{
//if something fails then delete the dummy message
			mailMessageRepo.deleteById(messageId);
		}
	}
}
