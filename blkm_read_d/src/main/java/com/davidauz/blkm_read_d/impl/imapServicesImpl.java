package com.davidauz.blkm_read_d.impl;

import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.davidauz.blkm_read_d.service.imapServices;
import jakarta.mail.Folder;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class imapServicesImpl implements imapServices {

	private static final String BM_PROCESSED_FOLDER = "bm_processed";
	private final Logger logger = LoggerFactory.getLogger(imapServicesImpl.class);

	@Autowired
	private ConfigurationRepository cfgRepo;

	private Store store=null;

	@Override
	public boolean is_IMAP_store_ok() {
		try {
			store= get_imap_store();
			Folder processed_folder = store.getFolder(BM_PROCESSED_FOLDER);
			if(! processed_folder.exists() ){
				boolean created = processed_folder.create(Folder.HOLDS_MESSAGES);
				if(!created)
					throw new Exception("Cannot create folder `"+BM_PROCESSED_FOLDER+"`");
			}
			return true;
		} catch (Exception e) {
			store=null;
			logger.error("Cannot open IMAP store: "+e.getMessage());
		}
		return false;
	}

	public Store get_imap_store() throws Exception {
		if(null!=store)
			return store;
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
		store = session.getStore("imap");
		store.connect(imap_host, imap_username, imap_password);
		return store;
	}
}
