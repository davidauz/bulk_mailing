package com.davidauz.bulk_mailing.blkm_common.entity;

import com.davidauz.bulk_mailing.blkm_common.entity.blk_MailMessage;

public interface blk_MailQueue {
    public void enqueue(blk_MailMessage mmessage);
}
