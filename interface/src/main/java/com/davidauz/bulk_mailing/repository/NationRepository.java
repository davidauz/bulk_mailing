package com.davidauz.bulk_mailing.repository;

import com.davidauz.bulk_mailing.entity.Company;
import com.davidauz.bulk_mailing.entity.Nation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NationRepository extends JpaRepository<Nation, Long> {
    Page<Company> findByNationContainingIgnoreCase(String keyword, Pageable paging);
}


