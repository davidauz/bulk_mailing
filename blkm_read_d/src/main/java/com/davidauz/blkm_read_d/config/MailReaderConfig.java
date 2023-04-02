package com.davidauz.blkm_read_d.config;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Optional;
import java.util.Properties;

@Configuration
public class MailReaderConfig {

    @Autowired
    private ConfigurationRepository cfgRepo;


    private static final String[][] MANDATORY_PROPERTIES =
    {   {"imap_server","example.imap.com"} // mail.imap.host
    ,	{"imap_uname", "myImapUserName"} // mail.imap.user
    ,	{"imap_password", "superSecret"} // mail.imap.password
    ,   {"imap_server_port","143"}
    ,   {"imap_auth","1"}           //mail.imap.auth
    ,	{"imap_auth_type","SSL"}//mail.imap.ssl.enable
    };

    private static final String[][] DEFAULT_PROPERTIES =
    {	{"heartbeat_r","0"}
    ,	{"read_mail_daemon_running","0"}
    };


    public void ensureValuesPresent() { // move this in common and get rid of the corresponding func in sender
        ConfigurationPair cp;
        Optional<ConfigurationPair> cpo;
        for(String[] prop:MANDATORY_PROPERTIES) {
            cpo = cfgRepo.findByName(prop[0]);
            if(!cpo.isPresent()){
                cp=new ConfigurationPair();
                cp.setName(prop[0]);
                cp.setValue(prop[1]);
                cfgRepo.save(cp);
            }
        }

        for(String[] prop:DEFAULT_PROPERTIES) {
            cpo = cfgRepo.findByName(prop[0]);
            if(!cpo.isPresent()){
                cp=new ConfigurationPair();
                cp.setName(prop[0]);
                cp.setValue(prop[1]);
                cfgRepo.save(cp);
            } else{
                cp=cpo.get();
                cp.setValue(prop[1]);
                cfgRepo.save(cp);
            }
        }
    }
}
