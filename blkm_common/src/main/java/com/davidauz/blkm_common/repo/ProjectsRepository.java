package com.davidauz.blkm_common.repo;

import com.davidauz.blkm_common.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectsRepository extends JpaRepository<Project, Long> {
    Page<Project> findByMailSubjectContainingIgnoreCase(String keyword, Pageable paging);
}


