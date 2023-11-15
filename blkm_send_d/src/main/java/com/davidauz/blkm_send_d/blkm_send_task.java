package com.davidauz.blkm_send_d;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class blkm_send_task implements Runnable {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private sendEmailService sendEmailSrvc;

    @Autowired
	MailMessageRepository mailMessageRepository;

    @Override
    public void run(){
        Optional<ConfigurationPair> o_mda = configurationRepository.findByName("send_mail_daemon_running");
        if(o_mda.isPresent()){
            ConfigurationPair mda=o_mda.get();
            if("0".equals(mda.getValue()))
                return;
            mailMessageRepository.update_heart_beat("heartbeat_s");

            sendEmailSrvc.send_messages_in_queue();
        }
    }
}
