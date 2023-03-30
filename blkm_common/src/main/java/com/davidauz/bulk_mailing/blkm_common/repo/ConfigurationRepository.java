package com.davidauz.bulk_mailing.blkm_common.repo;

import com.davidauz.bulk_mailing.blkm_common.entity.ConfigurationPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationPair, Long> {
    Optional<ConfigurationPair> findByName(String name);
}


