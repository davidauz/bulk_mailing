package com.davidauz.bulk_mailing.common_classes.repo;

import com.davidauz.bulk_mailing.common_classes.entity.ConfigurationPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationPair, Long> {
    Optional<ConfigurationPair> findByName(String name);
}


