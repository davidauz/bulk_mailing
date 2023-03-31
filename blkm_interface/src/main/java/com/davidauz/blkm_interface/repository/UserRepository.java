package com.davidauz.blkm_interface.repository;

import com.davidauz.blkm_interface.entity.User;
import com.davidauz.blkm_interface.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
