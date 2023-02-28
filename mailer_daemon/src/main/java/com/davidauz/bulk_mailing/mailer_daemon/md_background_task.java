package com.davidauz.bulk_mailing.mailer_daemon;

import com.davidauz.bulk_mailing.common_classes.entity.ConfigurationPair;
import com.davidauz.bulk_mailing.common_classes.entity.blk_MailMessage;
import com.davidauz.bulk_mailing.common_classes.entity.blk_MailQueue;
import com.davidauz.bulk_mailing.common_classes.repo.ConfigurationRepository;
import com.davidauz.bulk_mailing.common_classes.repo.MailMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

public class md_background_task implements Runnable {

    @Autowired
    private blk_MailQueue blk_mailQueue;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private sendEmailService sendEmailSrvc;

    @Autowired
    private MailMessageRepository mailMessageRepository;

    @Override
    public void run(){
        Optional<ConfigurationPair> o_mda = configurationRepository.findByName("mailer_daemon_active");
        if(o_mda.isPresent()){
            ConfigurationPair mda=o_mda.get();
            if("0".equals(mda.getValue()))
                return;
            mailMessageRepository.update_heart_beat();

            sendEmailSrvc.send_messages_in_queue();
        }
    }
}
