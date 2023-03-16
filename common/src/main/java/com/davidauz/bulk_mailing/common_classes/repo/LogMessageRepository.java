package com.davidauz.bulk_mailing.common_classes.repo;

import com.davidauz.bulk_mailing.common_classes.entity.LogItem;
import com.davidauz.bulk_mailing.common_classes.entity.blk_MailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface LogMessageRepository extends JpaRepository<LogItem, Long> {
    Optional<blk_MailMessage> findById(String name);
}


