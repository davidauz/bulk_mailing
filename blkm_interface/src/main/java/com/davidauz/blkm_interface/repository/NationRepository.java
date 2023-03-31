package com.davidauz.blkm_interface.repository;

import com.davidauz.blkm_interface.entity.Company;
import com.davidauz.blkm_interface.entity.Nation;
import com.davidauz.blkm_interface.entity.Company;
import com.davidauz.blkm_interface.entity.Nation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NationRepository extends JpaRepository<Nation, Long> {
    Page<Company> findByNationContainingIgnoreCase(String keyword, Pageable paging);
}


