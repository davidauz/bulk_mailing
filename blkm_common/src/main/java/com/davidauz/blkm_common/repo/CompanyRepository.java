package com.davidauz.blkm_common.repo;

import com.davidauz.blkm_common.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findByNameContainingIgnoreCase(String keyword, Pageable paging);
}


