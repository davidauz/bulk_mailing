package com.davidauz.blkm_read_d.service;

import jakarta.mail.Store;

public interface imapServices {
	boolean is_IMAP_store_ok();
	Store get_imap_store() throws Exception;
}
