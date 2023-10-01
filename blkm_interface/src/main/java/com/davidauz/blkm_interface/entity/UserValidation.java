package com.davidauz.blkm_interface.entity;

import com.davidauz.blkm_interface.util.TimeUtil;
import static com.davidauz.blkm_interface.controller.AuthController.TOKEN_TYPE_PASSWORD_RESET;

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
public class UserValidation
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String token;

    private Timestamp tokenTimeStamp;

    @Column(name = "creation")
    @CreationTimestamp
    private Timestamp creation;

    @Version
    @Column(name = "entity_version", nullable = false)
    private Long tokenType;

    @Column(name = "user_id", nullable = false)
    private Long user;

    public UserValidation(User newUser) {
        this.user = newUser.getId();
    }

    public void newToken(long tokenType) {
        setToken(UUID.randomUUID().toString());
        setTokenTimeStamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
        setTokenType(tokenType);
    }


    public boolean PwdResettokenIsCurrent() {
        TimeUtil time = new TimeUtil();
        long a=Math.abs(getTokenTimeStamp().getTime());
        long b = time.now().getTime();
        long diff=Math.abs(getTokenTimeStamp().getTime() - time.now().getTime());
        return diff < 1000 * 60 * 60 * 24;
    }

    public boolean tokenIsCurrent() {
        TimeUtil time = new TimeUtil();
        long a=Math.abs(getTokenTimeStamp().getTime());
        long b = time.now().getTime();
        return Math.abs(getTokenTimeStamp().getTime() - time.now().getTime()) < 1000 * 60 * 60 * 24;
    }

    public boolean passwordValidationIsCurrent() {
        TimeUtil time = new TimeUtil();
        return Math.abs(getTokenTimeStamp().getTime() - time.now().getTime()) < 1000 * 60 * 5;
    }

}
