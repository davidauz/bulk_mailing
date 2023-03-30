package com.davidauz.bulk_mailing.mailer_daemon;

import com.davidauz.bulk_mailing.blkm_common.entity.*;
import com.davidauz.bulk_mailing.blkm_common.repo.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

public class md_background_task implements Runnable {

    @Autowired
    blk_MailQueue blk_mailQueue;

    @Autowired
    private com.davidauz.bulk_mailing.blkm_common.repo.ConfigurationRepository configurationRepository;

    @Autowired
    private sendEmailService sendEmailSrvc;

    @Autowired
    MailMessageRepository mailMessageRepository;

    @Override
    public void run(){
        Optional<ConfigurationPair> o_mda = configurationRepository.findByName("mailer_daemon_running");
        if(o_mda.isPresent()){
            ConfigurationPair mda=o_mda.get();
            if("0".equals(mda.getValue()))
                return;
            mailMessageRepository.update_heart_beat();

            sendEmailSrvc.send_messages_in_queue();
        }
    }
}
