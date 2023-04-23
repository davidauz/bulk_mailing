package com.davidauz.blkm_common.repo;

import com.davidauz.blkm_common.entity.LogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogMessageRepository extends JpaRepository<LogItem, Long> {
}


