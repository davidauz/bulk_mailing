package com.davidauz.blkm_common.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class reportsDTO {
	@Id
	private Long mailId;

	private Long  projectId ;
	private String description;
	private String Result;
	private String recipient;
	private Long idRecipient;
	private String sentStatus;
	private String subject;
}
