package com.davidauz.blkm_interface.repository;

import com.davidauz.blkm_interface.entity.Role;
import com.davidauz.blkm_interface.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
