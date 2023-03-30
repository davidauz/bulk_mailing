package com.davidauz.bulk_mailing.blkm_common.repo;

import com.davidauz.bulk_mailing.blkm_common.entity.blk_MailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface MailMessageRepository extends JpaRepository<blk_MailMessage, Long> {
    Optional<blk_MailMessage> findById(String name);
    Long countBySentStatus(blk_MailMessage.SENT_STATUS param_status);

    @Transactional // needs to be executed inside a transaction
    @Modifying
    @Query(value = "update config set value=1+value where name='heartbeat';", nativeQuery = true)
    void update_heart_beat();

    @Query (value="SELECT coalesce(min(id), 0) FROM mail_message WHERE sent_status=:#{#paramstatus.name()}", nativeQuery = true)
    long getMinId(@Param("paramstatus") blk_MailMessage.SENT_STATUS paramstatus);
}


