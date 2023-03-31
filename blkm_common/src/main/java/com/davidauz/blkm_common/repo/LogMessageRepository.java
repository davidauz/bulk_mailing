package com.davidauz.blkm_common.repo;

import com.davidauz.blkm_common.entity.LogItem;
import com.davidauz.blkm_common.entity.blk_MailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LogMessageRepository extends JpaRepository<LogItem, Long> {
    Optional<blk_MailMessage> findById(String name);
}


