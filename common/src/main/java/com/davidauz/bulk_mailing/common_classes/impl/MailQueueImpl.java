package com.davidauz.bulk_mailing.common_classes.impl;

import com.davidauz.bulk_mailing.common_classes.entity.ConfigurationPair;
import com.davidauz.bulk_mailing.common_classes.entity.blk_MailMessage;
import com.davidauz.bulk_mailing.common_classes.entity.blk_MailQueue;
import com.davidauz.bulk_mailing.common_classes.repo.ConfigurationRepository;
import com.davidauz.bulk_mailing.common_classes.repo.MailMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

@Service
public class MailQueueImpl implements blk_MailQueue {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private MailMessageRepository mailMessageRepo;



    @Override
    public void enqueue(blk_MailMessage mmessage) {
        mmessage.setSentStatus(blk_MailMessage.SENT_STATUS.ENQUEUED);
        mmessage.setTimeEnqueued(new Timestamp(Calendar.getInstance().getTime().getTime()));
        mailMessageRepo.save(mmessage);
    }



}
