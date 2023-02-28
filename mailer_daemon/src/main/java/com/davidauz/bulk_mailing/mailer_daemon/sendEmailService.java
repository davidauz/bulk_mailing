package com.davidauz.bulk_mailing.mailer_daemon;

import com.davidauz.bulk_mailing.common_classes.entity.ConfigurationPair;
import com.davidauz.bulk_mailing.common_classes.entity.blk_MailMessage;
import com.davidauz.bulk_mailing.common_classes.repo.ConfigurationRepository;
import com.davidauz.bulk_mailing.common_classes.repo.MailMessageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

@Service
public class sendEmailService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    private final Logger logger = LoggerFactory.getLogger(sendEmailService.class);

    @Autowired
    private JavaMailSender JMailSender;

    @Autowired
    private MailMessageRepository mailMessageRepo;

    @Value("${mail.test:false}")
    private Boolean mailTest = false;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        if (mailTest) {
            logger.error(email.getText());
        } else {
            try {
                JMailSender.send(email);
            } catch (Exception e) {
                logger.error("Unable to send email! Future emails will be logged - no retry!", e);
                mailTest = true;
                logger.error(email.getText());
            }
        }
    }

    void sendOneEmail(long mail_id) throws Exception {
        Optional<blk_MailMessage> o_blkm = Optional.ofNullable(mailMessageRepo.findById(mail_id).orElseThrow(() -> new Exception("mail ID '" + mail_id + "' not found")));
        blk_MailMessage blkm=o_blkm.get();
        final MimeMessage mimeMessage = JMailSender.createMimeMessage();
//        final String from = "noreply etc";
        final String text_body = blkm.getBody();
        final MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setFrom(from);
            helper.setTo(blkm.getRecipient());
            helper.setSubject(blkm.getSubject());
            helper.setText(text_body, true);
        } catch (MessagingException e) {
            logger.error("Error in message id=`"+mail_id+"`");
            blkm.setSentStatus(blk_MailMessage.SENT_STATUS.SYSTEM_ERROR);
            return;
        }

//        if(attachments != null) {
//            for(final EmailAttachmentDTO attachment : attachments) {
//                final String attachmentFileName = attachment.getAttachmentFileName();
//                final String attachmentFilePath = attachment.getAttachmentFilePath();
//                final Path attachmentPath = Paths.get(attachmentFilePath);
//                final FileSystemResource attachmentFile = new FileSystemResource(attachmentPath);
//                helper.addAttachment(attachmentFileName, attachmentFile);
//            }
//        }

        JMailSender.send(mimeMessage);
    }

    public void send_messages_in_queue() {
        ConfigurationPair mda;
        Optional<ConfigurationPair> o_mda;
        Timestamp now_timestamp
                ,        last_sent_timestamp
                ;
        long minimum_time_diff=0
                ,   present_time_diff
                ;

        if(0==mailMessageRepo.countBySentStatus(blk_MailMessage.SENT_STATUS.ENQUEUED))
            return;
        o_mda=configurationRepository.findByName("last_send_timestamp");
        if(o_mda.isPresent()){
            now_timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            mda=o_mda.get();
            last_sent_timestamp=Timestamp.valueOf(mda.getValue());
            o_mda=configurationRepository.findByName("sendingRandomDelay");
            if(o_mda.isPresent())
                minimum_time_diff=Long.valueOf(o_mda.get().getValue());
            present_time_diff=now_timestamp.getNanos()-last_sent_timestamp.getNanos();
            present_time_diff*=1000;
            if(present_time_diff<minimum_time_diff)
                return;
        }
        long smallest_id=mailMessageRepo.getMinId(blk_MailMessage.SENT_STATUS.ENQUEUED);
        blk_MailMessage blkmm=mailMessageRepo.getReferenceById(smallest_id);
        try {
            sendOneEmail(smallest_id);
            blkmm.setSentStatus(blk_MailMessage.SENT_STATUS.SENT_SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        blkmm.setSentStatus(blk_MailMessage.SENT_STATUS.SENT_ERROR);
    }
}
