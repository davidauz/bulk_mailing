package com.davidauz.blkm_send_d;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

@Service
public class sendEmailService {


    @Autowired
    private ConfigurationRepository cfgRepo;

    private final Logger logger = LoggerFactory.getLogger(sendEmailService.class);

    @Autowired
    private JavaMailSender JMailSender;

    @Autowired
    private MailMessageRepository mailMessageRepo;

    void sendOneEmail(long mail_id) throws Exception {
        Optional<blk_MailMessage> o_blkm = Optional.ofNullable(mailMessageRepo.findById(mail_id).orElseThrow(() -> new Exception("mail ID '" + mail_id + "' not found")));
        Optional<ConfigurationPair> o_mda= cfgRepo.findByName("last_send_timestamp"); // TODO: this is called at every email, optimize
        ConfigurationPair mda;
        if(o_mda.isPresent())
            mda=o_mda.get();
        else{
            mda=new ConfigurationPair();
            mda.setName("last_send_timestamp");
        }
        blk_MailMessage blkm=o_blkm.get();

        final MimeMessage mimeMessage = JMailSender.createMimeMessage();
        final String text_body = blkm.getBody();
        final MimeMessageHelper helper;
        Timestamp now_timestamp;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(cfgRepo.findByName("serveruname").get().getValue());
            helper.setTo(blkm.getRecipient());
            helper.setSubject(blkm.getSubject());
            helper.setText(text_body, true);
            JMailSender.send(mimeMessage);
            now_timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            blkm.setTimeSent(now_timestamp);
            blkm.setSentStatus(blk_MailMessage.SENT_STATUS.SENT_SUCCESS);
            mailMessageRepo.save(blkm);
            mda.setValue(String.valueOf( now_timestamp ));
            cfgRepo.save(mda);
        } catch (MessagingException e) {
            logger.error("Error in message id=`"+mail_id+"`");
            blkm.setSentStatus(blk_MailMessage.SENT_STATUS.SYSTEM_ERROR);
            mailMessageRepo.save(blkm);
        }

//        if(attachments != null) { // TODO
//            for(final EmailAttachmentDTO attachment : attachments) {
//                final String attachmentFileName = attachment.getAttachmentFileName();
//                final String attachmentFilePath = attachment.getAttachmentFilePath();
//                final Path attachmentPath = Paths.get(attachmentFilePath);
//                final FileSystemResource attachmentFile = new FileSystemResource(attachmentPath);
//                helper.addAttachment(attachmentFileName, attachmentFile);
//            }
//        }


    }

    public void send_messages_in_queue() {
        ConfigurationPair mda;
        Optional<ConfigurationPair> o_mda;
        Timestamp now_timestamp
        ,        last_sent_timestamp
        ;
        long minimum_time_diff=0
        ,   present_time_diff
        ,   smallest_id=0
        ;

        if(0==mailMessageRepo.countBySentStatus(blk_MailMessage.SENT_STATUS.ENQUEUED))
            return;
        try{
        o_mda= cfgRepo.findByName("last_send_timestamp");
        if(o_mda.isPresent()){
            now_timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            mda=o_mda.get();
            last_sent_timestamp=Timestamp.valueOf(mda.getValue());
            logger.info("last_send_timestamp="+last_sent_timestamp);
            o_mda= cfgRepo.findByName("sendingRandomDelay");
            if(o_mda.isPresent())
                minimum_time_diff=Long.valueOf(o_mda.get().getValue());
            logger.info("minimum_time_diff="+minimum_time_diff);
            long nano1=now_timestamp.getTime();
            long nano2=last_sent_timestamp.getTime();
            present_time_diff=now_timestamp.getTime()-last_sent_timestamp.getTime();
            logger.info("present_time_diff="+present_time_diff);
            if(present_time_diff<minimum_time_diff)
                Thread.sleep(minimum_time_diff);
        }
            smallest_id=mailMessageRepo.getMinId(blk_MailMessage.SENT_STATUS.ENQUEUED);
            sendOneEmail(smallest_id);
        } catch (Exception e) {
            logger.error("Error in message id=`"+smallest_id+"`");
            throw new RuntimeException(e);
        }

    }
}
