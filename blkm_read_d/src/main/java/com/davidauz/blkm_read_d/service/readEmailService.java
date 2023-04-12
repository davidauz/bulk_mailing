package com.davidauz.blkm_read_d.service;

import com.davidauz.blkm_common.entity.EmailStatusConstants;
import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_read_d.entity.blkMessageInfo;
import jakarta.mail.*;
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
        try {
            Store store =imapservices.get_imap_store();

            Folder inbox = store.getFolder("INBOX")
            ,   processed=store.getFolder("bm_processed")
            ;
            inbox.open(Folder.READ_WRITE);
            processed.open(Folder.READ_WRITE);

//            Flags flags = new Flags();
//            flags.add(Flags.Flag.SEEN);
//            flags.add(Flags.Flag.RECENT);

// Get messages in the inbox folder
            Message[] arr_messages = inbox.search(
//              new FlagTerm( flags, true) // TODO: decide if it is SEEN or RECENT
            new FlagTerm( new Flags(Flags.Flag.SEEN), false)
//                new FlagTerm(new Flags(Flags.Flag.RECENT), false)
            );
            logger.info("Got `"+arr_messages.length+"` messages");
            ArrayList<Message> mList=new ArrayList<>();
            for (Message one_message : arr_messages) {
                processMessage(one_message);
                mList.add(one_message);
            }
            Message[] moved = mList.toArray(new Message[mList.size()]);
            inbox.copyMessages(moved, processed);
            processed.close(true);
            for(Message one_message: moved)
                one_message.setFlag(Flags.Flag.DELETED, true);
            inbox.close(true);
        }catch(Exception e){
            logger.info("Exception: "+e.getMessage());
        }
    }

    private void processMessage(Message message) {
        msg_i=new blkMessageInfo();
        InputStream istr = null;
        String line;
        try {
            istr = message.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(istr));
            while ((line = reader.readLine()) != null)
                parseLine(line, msg_i);
            reader.close();
            update_status(msg_i);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
        blkm.setSentStatus(msg_i.getSntStatus());
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
        if (line.contains("550")) {
            bmi.setSntStatus(EmailStatusConstants.ERR_RECIPIENT_NOT_FOUND);
            bmi.setStrContent(line);
        }
        if (line.contains("554")) {
            bmi.setSntStatus(EmailStatusConstants.ERR_RECIPIENT_MAILBOX_FULL);
            bmi.setStrContent(line);
        }
        if (line.contains("552")) {
            bmi.setSntStatus(EmailStatusConstants.ERR_MAILBOX_NOT_FOUND);
            bmi.setStrContent(line);
        }
        if (line.contains("server unavailable")) {
            bmi.setSntStatus(EmailStatusConstants.ERR_SERVER_UNAVAILABLE);
            bmi.setStrContent(line);
        }

    }


}
