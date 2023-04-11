package com.davidauz.blkm_read_d.entity;


import com.davidauz.blkm_common.entity.blk_MailMessage;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class blkMessageInfo {
	private String strContent;

	private String strMessageId;

	private String sntStatus;

	public void init() {
		strMessageId = "";
		strContent="";
	}
}
