package com.davidauz.blkm_common.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


//TODO: this very likely not necerssary

@Entity
public class reportsDTO {
	private Long  projectId ;
	private String description;
	private String Result;

	@Id
	private Long mailId;
	private String recipient;
	private String sentStatus;
	private String subject;


	public reportsDTO(Long projectId, String description, @Nullable String Result, Long mailId, String recipient, String sentStatus, String subject) {
		this.projectId = projectId;
		this.description = description;
		this.Result = Result;
		this.mailId = mailId;
		this.recipient = recipient;
		this.sentStatus = sentStatus;
		this.subject = subject;
	}

	public reportsDTO() {
		int a=1;
	}

	public Long getProjectId() {
		return this.projectId;
	}

	public String getDescription() {
		return this.description;
	}

	public String getResult() {
		return this.Result;
	}

	public Long getMailId() {
		return this.mailId;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public String getSentStatus() {
		return this.sentStatus;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setResult(String Result) {
		this.Result = Result;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setSentStatus(String sentStatus) {
		this.sentStatus = sentStatus;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
