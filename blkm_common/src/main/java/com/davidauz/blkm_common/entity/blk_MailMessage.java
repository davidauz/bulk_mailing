package com.davidauz.blkm_common.entity;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="mail_message")
public class blk_MailMessage {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String subject;

    @Column
    private Long idRecipient;

    @Column(nullable=false)
    private String recipient;

    @Column
    private String Result;

    @Column
    private String messageId;

    @Column(nullable=false)
    private Timestamp timeEnqueued;

    @Column
    private Timestamp timeSent;

    @Column(nullable=false)
    private String sentStatus;

    @Column
    private Long idCampaign;

    @Column(nullable=false, columnDefinition="TEXT")
    @Size(min=2)
    private String body;


    public void addInline(String logo, ClassPathResource classPathResource) {
    }

}
