package com.davidauz.bulk_mailing.repository;

import com.davidauz.bulk_mailing.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
