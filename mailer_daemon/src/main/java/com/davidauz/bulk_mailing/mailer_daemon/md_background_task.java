package com.davidauz.bulk_mailing.mailer_daemon;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.entity.blk_MailQueue;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class md_background_task implements Runnable {

    @Autowired
	blk_MailQueue blk_mailQueue;

    @Autowired
    private ConfigurationRepository configurationRepository;

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
