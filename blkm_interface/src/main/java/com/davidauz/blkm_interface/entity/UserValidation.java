package com.davidauz.blkm_interface.entity;

import com.davidauz.blkm_interface.util.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_validation")
public class UserValidation //implements Serializable: questo provoca eccezione
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String token;

    private Timestamp tokenIssue;

    @Column(name = "pass_reset_token")
    private String passwordResetToken;

    @Column(name = "pass_reset_issue") // TODO: one field for issue type, one field for token
    private Timestamp passwordResetIssue;

    @Column(name = "creation")
    @CreationTimestamp
    private Timestamp creation;

    @Version
    @Column(name = "entity_version", nullable = false)
    private Long version;

    @Column(name = "user_id", nullable = false)
    private Long user;

    public UserValidation(User newUser) {
        this.user = newUser.getId();
    }

    public void newToken() {
        setToken(UUID.randomUUID().toString());
        setTokenIssue(new Timestamp(Calendar.getInstance().getTime().getTime()));
    }

    public Timestamp getTokenIssue() {
        return tokenIssue;
    }

    public void setTokenIssue(Timestamp tokenIssue) {
        this.tokenIssue = tokenIssue;
    }


    public boolean PwdResettokenIsCurrent() {
        TimeUtil time = new TimeUtil();
        long a=Math.abs(getPasswordResetIssue().getTime());
        long b = time.now().getTime();
        return Math.abs(getPasswordResetIssue().getTime() - time.now().getTime()) < 1000 * 60 * 60 * 24;
    }

    public boolean tokenIsCurrent() {
        TimeUtil time = new TimeUtil();
        long a=Math.abs(getTokenIssue().getTime());
        long b = time.now().getTime();
        return Math.abs(getTokenIssue().getTime() - time.now().getTime()) < 1000 * 60 * 60 * 24;
    }

    public boolean passwordValidationIsCurrent() {
        TimeUtil time = new TimeUtil();
        return Math.abs(getPasswordResetIssue().getTime() - time.now().getTime()) < 1000 * 60 * 5;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Timestamp getPasswordResetIssue() {
        return passwordResetIssue;
    }

    public void setPasswordResetIssue(Timestamp passwordResetIssue) {
        this.passwordResetIssue = passwordResetIssue;
    }

    public void newPasswordResetToken() {
        setPasswordResetToken(UUID.randomUUID().toString());
        setPasswordResetIssue(new Timestamp(Calendar.getInstance().getTime().getTime()));
    }
}
