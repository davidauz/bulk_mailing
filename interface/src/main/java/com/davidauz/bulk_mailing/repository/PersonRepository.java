package com.davidauz.bulk_mailing.repository;

import com.davidauz.bulk_mailing.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Page<Person> findByFirstNameContainingIgnoreCase(String keyword, Pageable paging);
    List<Person> findByCompany_Id( Long comp_id);
}


