package com.davidauz.blkm_read_d.service;

import com.davidauz.blkm_common.entity.EmailStatusConstants;
import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_read_d.entity.blkMessageInfo;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class readEmailService {

    @Autowired
    private imapServices imapservices;


    @Autowired
    private ConfigurationRepository cfgRepo;

    private final Logger logger = LoggerFactory.getLogger(readEmailService.class);

    @Autowired
    private MailMessageRepository mailMessageRepo;

    private blkMessageInfo msg_i;

    public void read_messages_in_inbox() {
        ArrayList<Message> mList=new ArrayList<>();
        Folder inbox = null
        ,   processed=null
        ;
        try {
            Store store =imapservices.get_imap_store();
            inbox = store.getFolder("INBOX");
            processed=store.getFolder("bm_processed");
            inbox.open(Folder.READ_WRITE);
            processed.open(Folder.READ_WRITE);
            Message[] arr_messages = inbox.getMessages();
            logger.info("Got `"+arr_messages.length+"` messages");
            for (Message msg : arr_messages) {
                try {
                    processMessage(msg);
                }catch(MessagingException messEx){
                    if (messEx.getMessage() != null && messEx.getMessage().toLowerCase().contains("unable to load " +"bodystructure")) {
                        MimeMessage msgDownloaded = new MimeMessage((MimeMessage) msg);
                        processMessage(msgDownloaded);
                    }else{
                        logger.error("Unrecoverable error: "+messEx.getMessage());
                    }
                }
                mList.add(msg);
            }
        }catch(Exception e){
            logger.info("Exception: "+e.getMessage());
        } finally{
            Message[] moved = mList.toArray(new Message[mList.size()]);
            try {
                inbox.copyMessages(moved, processed);
                processed.close(true);
                for(Message one_message: moved)
                    one_message.setFlag(Flags.Flag.DELETED, true);
                inbox.close(true);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processMessage(Message message) throws MessagingException, IOException {
        msg_i=new blkMessageInfo();
        InputStream istr = null;
        BufferedReader reader=null;
        String line;
        try {
            istr = message.getInputStream();
            reader = new BufferedReader(new InputStreamReader(istr));
            while ((line = reader.readLine()) != null)
                parseLine(line, msg_i);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw(e);
        }
        finally {
            reader.close();
            update_status(msg_i);
        }
    }


    private void update_status(blkMessageInfo msg_i) {
        Optional<blk_MailMessage> oblkm = mailMessageRepo.findByMessageId(msg_i.getStrMessageId());
        if(!oblkm.isPresent()){
            logger.info("`"+msg_i.getStrMessageId()+"` not found");
            return;
        }
        blk_MailMessage blkm=oblkm.get();
        blkm.setResult(msg_i.getStrContent());
        blkm.setSentStatus(msg_i.getSntStatus());
        if(null==blkm.getSentStatus())
            blkm.setSentStatus(EmailStatusConstants.ERR_UNDEFINED);
        mailMessageRepo.save(blkm);
    }





    private void parseLine
    (   String line
    ,   blkMessageInfo bmi
    )
    {
        logger.info(line);
        if (line.contains("Message-ID")&&line.contains("bm.")) {
            line = line.substring(line.indexOf("bm.") , 4+line.indexOf(".com"));
            bmi.setStrMessageId(line);
        }
        if(null!=bmi.getSntStatus() && 0!=bmi.getSntStatus().length())
                return;
        if(line.contains("Part_"))
            return;
        for(String[] str : EmailStatusConstants.error_codes_table) {
            if (line.contains(str[0])) {
                System.out.println("---------------------------"+line+"-------------------------"+str[0]+"-----------------");
                bmi.setSntStatus(str[1]);
                bmi.setStrContent(line);
            }
        }
    }


}
