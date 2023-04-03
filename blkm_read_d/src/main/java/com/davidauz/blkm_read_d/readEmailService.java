package com.davidauz.blkm_read_d;

import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class readEmailService {


    @Autowired
    private ConfigurationRepository cfgRepo;

    private final Logger logger = LoggerFactory.getLogger(readEmailService.class);

    @Autowired
    private MailMessageRepository mailMessageRepo;

    public void read_messages_in_inbox() {
        try {
            String imap_host=cfgRepo.findByName("imap_server").orElseThrow(()->new Exception("imap_server cfg not found")).getValue()
            ,   imap_username=cfgRepo.findByName("imap_uname").orElseThrow(()->new Exception("imap_uname cfg not found")).getValue()
            ,   imap_password=cfgRepo.findByName("imap_password").orElseThrow(()->new Exception("imap_password cfg not found")).getValue()
            ,   imap_server_port=cfgRepo.findByName("imap_server_port").orElseThrow(()->new Exception("imap_server_port cfg not found")).getValue()
            ,   imap_ssl=""
            ;

            if(cfgRepo.findByName("imap_ssl").isPresent())
                imap_ssl=cfgRepo.findByName("imap_ssl").get().getValue();

            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imap");
            props.setProperty("mail.imap.host", imap_host);
            props.setProperty("mail.imap.user", imap_username);
            props.setProperty("mail.imap.password", imap_password);
            props.setProperty("mail.imap.port", imap_server_port);
            props.setProperty("mail.imap.auth", "true");
            if(null!=imap_ssl && imap_ssl.equals("on"))
                props.setProperty("mail.imap.ssl.enable", "true");

            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(imap_host, imap_username, imap_password);

            // Open the inbox folder in read-only mode
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Get all the messages in the inbox folder
            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false));
//            new FlagTerm(new Flags(Flags.Flag.RECENT), false)); // DEBUG

            // Print the subject of each unread message
            for (Message message : messages) {
                String[] messageID = message.getHeader("In-Reply-To");
                System.out.println(message.getSubject());
            }

            // Close the folder and the store
            inbox.close(false);
            store.close();
        }catch(Exception e){
            logger.info("Exception: "+e.getMessage());
        }
    }
}
