package com.davidauz.blkm_read_d;

import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_read_d.entity.blkMessageInfo;
import com.sun.mail.imap.IMAPNestedMessage;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FlagTerm;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

@Service
public class readEmailService {


    @Autowired
    private ConfigurationRepository cfgRepo;

    private final Logger logger = LoggerFactory.getLogger(readEmailService.class);

    @Autowired
    private MailMessageRepository mailMessageRepo;

    private blkMessageInfo msg_i;

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

// Get messages in the inbox folder
            Message[] messages = inbox.search(

//              new FlagTerm( new Flags(Flags.Flag.SEEN), false)
                new FlagTerm(new Flags(Flags.Flag.RECENT), false)

            );
            logger.info("Got `"+messages.length+"` messages");
            for (Message message : messages){
                msg_i=new blkMessageInfo();
                InputStream is = message.getInputStream();
                // read message content and write to file
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null)
                    parseLine(line, msg_i);
                reader.close();
                update_status(msg_i);
            }
            inbox.close(false);
            store.close();
        }catch(Exception e){
            logger.info("Exception: "+e.getMessage());
        }
    }

    private void update_status(blkMessageInfo msg_i) {
        Optional<blk_MailMessage> oblkm = mailMessageRepo.findByMessageId(msg_i.getStrMessageId());
        if(!oblkm.isPresent()){
            logger.info("`"+msg_i+"` not found");
            return;
        }
        blk_MailMessage blkm=oblkm.get();
        blkm.setResult(msg_i.getStrContent());
        mailMessageRepo.save(blkm);
    }





    private void parseLine
    (   String line
    ,   blkMessageInfo bmi
    )
    {
        logger.info(line);
        if (line.contains("Message-ID")&&line.contains("bm.")) {
            line = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
            bmi.setStrMessageId(line);
        }
        if (line.contains("550"))
            bmi.setStrContent(line);
    }


}
