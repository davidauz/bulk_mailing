package com.davidauz.bulk_mailing.repository;

import com.davidauz.bulk_mailing.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Page<Group> findByGroupNameContainingIgnoreCase(String keyword, Pageable paging);
}


