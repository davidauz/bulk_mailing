package com.davidauz.blkm_read_d;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.entity.blk_MailQueue;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_read_d.service.readEmailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class blkm_read_task implements Runnable {

    @Autowired
	blk_MailQueue blk_mailQueue;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private readEmailService readEmailSrvc;

    @Autowired
	MailMessageRepository mailMessageRepository;

    @Override
    public void run(){
        Optional<ConfigurationPair> o_mda = configurationRepository.findByName("read_mail_daemon_running");
        if(o_mda.isPresent()){
            ConfigurationPair mda=o_mda.get();
            if("0".equals(mda.getValue()))
                return;
            mailMessageRepository.update_heart_beat_r();

            readEmailSrvc.read_messages_in_inbox();
        }
    }
}
