package com.davidauz.bulk_mailing.common_classes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="mail_message")
public class blk_MailMessage {
    private static final long serialVersionUID = 1L;


    public enum SENT_STATUS
    {   SENT_SUCCESS
    ,   SENT_ERROR
    ,   ENQUEUED
    ,   SYSTEM_ERROR
    }
    // COLUMNS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String subject;

    @Column(nullable=false)
    private String recipient;

    @Column(nullable=false)
    private Timestamp timeEnqueued;

    @Column
    private Timestamp timeSent;

    @Column
    @Enumerated(EnumType.STRING)
    private SENT_STATUS sentStatus;

    @Column
    private Long idCampaign;

    @Column(nullable=false, columnDefinition="TEXT")
    @Size(min=2)
    private String body;


    public void addInline(String logo, ClassPathResource classPathResource) {
    }

}
