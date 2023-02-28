package com.davidauz.bulk_mailing.common_classes.entity;

import com.davidauz.bulk_mailing.common_classes.entity.blk_MailMessage;

public interface blk_MailQueue {
    public void enqueue(blk_MailMessage mmessage);
}
