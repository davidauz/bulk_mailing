package com.davidauz.blkm_read_d.config;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_read_d.service.imapServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class MailReaderConfig {

    @Autowired
    private ConfigurationRepository cfgRepo;

    @Autowired private imapServices imapservices;

    private static final String[][] MANDATORY_PROPERTIES =
    {   {"imap_server","example.imap.com"} // mail.imap.host
    ,	{"imap_uname", "myImapUserName"} // mail.imap.user
    ,	{"imap_password", "superSecret"} // mail.imap.password
    ,   {"imap_server_port","143"}
    ,   {"imap_auth","1"}           //mail.imap.auth TODO: check if this is right
    ,	{"imap_auth_type","SSL"}//mail.imap.ssl.enable TODO: check if this is right
    };

    private static final String[][] DEFAULT_PROPERTIES =
    {	{"heartbeat_r","0"}
    ,	{"read_mail_daemon_running","0"}
    };


    public void ensureValuesPresent() { // move this in common and get rid of the corresponding func in sender
        ConfigurationPair cp;
        Optional<ConfigurationPair> cpo;

        if ( ! imapservices.is_IMAP_store_ok() )
            throw new RuntimeException("Cannot use IMAP folder"); // this will terminate the app
        // since spring.boot.exit-on-error=true in application.properties

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

    @PostConstruct
    public void executeAfterStartup() {
        ensureValuesPresent();
// some stuff to do before startup
    }
}
