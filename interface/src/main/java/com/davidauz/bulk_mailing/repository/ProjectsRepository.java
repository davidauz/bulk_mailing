package com.davidauz.bulk_mailing.repository;

import com.davidauz.bulk_mailing.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectsRepository extends JpaRepository<Project, Long> {
    Page<Project> findByNameContainingIgnoreCase(String keyword, Pageable paging);
}


