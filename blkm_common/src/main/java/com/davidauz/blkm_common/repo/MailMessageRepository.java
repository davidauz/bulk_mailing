package com.davidauz.blkm_common.repo;

import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.entity.reportsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface MailMessageRepository extends JpaRepository<blk_MailMessage, Long> {

    Optional<blk_MailMessage> findById(String name);

    Optional<blk_MailMessage> findByMessageId(String name);

    Page<blk_MailMessage> findBySubjectContaining(String keyword, Pageable paging);

    Long countBySentStatus(blk_MailMessage.SENT_STATUS param_status);

    @Transactional // needs to be executed inside a transaction
    @Modifying
    @Query(value = "update config set value=1+value where name='heartbeat_s';", nativeQuery = true)
    void update_heart_beat_s(); // TODO: pass update field name as parameter

    @Transactional // needs to be executed inside a transaction
    @Modifying
    @Query(value = "update config set value=1+value where name='heartbeat_r';", nativeQuery = true)
    void update_heart_beat_r(); // TODO: pass update field name as parameter

    @Query (value="SELECT coalesce(min(id), 0) FROM mail_message WHERE sent_status=:#{#paramstatus.name()}", nativeQuery = true)
    long getMinId(@Param("paramstatus") blk_MailMessage.SENT_STATUS paramstatus);

    @Query(value=
    " SELECT p.id projectId " +
    ", p.description " +
    ", mm.Result " +
    ", mm.id mailId " +
    ", mm.recipient " +
    ", mm.sentStatus " +
    ", mm.subject " +
    " FROM blk_MailMessage mm" +
    ", Project p" +
    " WHERE p.id=mm.idCampaign " +
    " AND (:project_desc IS NULL OR p.description=:project_desc) " +
    " AND (:subject IS NULL OR mm.subject=:subject) " +
    " AND (:Addresse IS NULL OR mm.recipient=:Addresse) " +
    " AND (:Status IS NULL OR mm.sentStatus=:Status) "
    )
    List<Object> findByParameters
    (	@Param("project_desc") String project_desc
    ,	@Param("subject") String subject
    ,	@Param("Addresse") String Addresse
    ,	@Param("Status") String Status
    );

}




