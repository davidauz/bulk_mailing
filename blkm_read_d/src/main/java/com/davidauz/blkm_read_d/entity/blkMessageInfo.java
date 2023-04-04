package com.davidauz.blkm_read_d.entity;


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

	public void init() {
		strMessageId = "";
		strContent="";
	}
}
