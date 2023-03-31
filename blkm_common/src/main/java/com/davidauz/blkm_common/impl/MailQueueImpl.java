package com.davidauz.blkm_common.impl;

import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.entity.blk_MailQueue;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

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
