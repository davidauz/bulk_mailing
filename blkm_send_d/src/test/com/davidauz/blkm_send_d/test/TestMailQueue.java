package com.davidauz.blkm_send_d.test;

import com.davidauz.blkm_common.entity.EmailStatusConstants;
import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.entity.blk_MailQueue;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootApplication
@SpringBootTest
//@ContextConfiguration
//@ComponentScan(basePackages={"com.davidauz.blkm_common","package com.davidauz.blkm_read_d"})
//@EntityScan(basePackages={"com.davidauz.blkm_common.*"})
//@DependsOn("mailMessageRepository")
public class TestMailQueue {

    @Autowired
    private blk_MailQueue mailQ;

    @Autowired
    private MailMessageRepository mailMessageRepo;

    @Test void testEnqueue() {

        Long lEnqueuedMessages=mailMessageRepo.countBySentStatus(EmailStatusConstants.ENQUEUED);

        blk_MailMessage blkmm=new blk_MailMessage();
        blkmm.setSubject("TEST subject");
        blkmm.setBody("TEST content");
        blkmm.setRecipient("someone@somewhere.net");
        blkmm.setIdRecipient(1L);
        blkmm.setIdCampaign(1L);
        mailQ.enqueue(blkmm);
        Long messageId = blkmm.getId();

        Long lEnqueuedMessages2=mailMessageRepo.countBySentStatus(EmailStatusConstants.ENQUEUED);

        assertEquals(lEnqueuedMessages, 1+lEnqueuedMessages2);

        mailMessageRepo.deleteById(messageId);

        lEnqueuedMessages2=mailMessageRepo.countBySentStatus(EmailStatusConstants.ENQUEUED);

        assertEquals(lEnqueuedMessages, lEnqueuedMessages2);
    }
}
