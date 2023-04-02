package com.davidauz.blkm_read_d;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;

@Service
public class readEmailService {


    @Autowired
    private ConfigurationRepository cfgRepo;

    private final Logger logger = LoggerFactory.getLogger(readEmailService.class);

    @Autowired
    private MailMessageRepository mailMessageRepo;

    @Value("${mail.test:false}")
    private Boolean mailTest = false;


    public void read_messages_in_inbox() {
        try {
            String imap_host=cfgRepo.findByName("imap_server").orElseThrow(()->new Exception("imap_server cfg not found")).getValue()
            ,   imap_username=cfgRepo.findByName("imap_uname").orElseThrow(()->new Exception("imap_uname cfg not found")).getValue()
            ,   imap_password=cfgRepo.findByName("imap_password").orElseThrow(()->new Exception("imap_password cfg not found")).getValue()
            ,   imap_server_port=cfgRepo.findByName("imap_server_port").orElseThrow(()->new Exception("imap_server_port cfg not found")).getValue()
            ;

            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            props.setProperty("mail.imaps.host", imap_host);
            props.setProperty("mail.imaps.user", imap_username);
            props.setProperty("mail.imaps.password", imap_password);
            props.setProperty("mail.imaps.port", imap_server_port);
            props.setProperty("mail.imaps.auth", "true");
            props.setProperty("mail.imaps.ssl.enable", "true");

            Session session = Session.getInstance(props);
            Store store = session.getStore("imaps");
            store.connect(imap_host, imap_username, imap_password);

            // Open the inbox folder in read-only mode
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Get all the messages in the inbox folder
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            // Print the subject of each unread message
            for (Message message : messages) {
                System.out.println(message.getSubject());
            }

            // Close the folder and the store
            inbox.close(false);
            store.close();
        }catch(Exception e){

        }
    }
}
