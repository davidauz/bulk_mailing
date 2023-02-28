package com.davidauz.bulk_mailing.repository;

import com.davidauz.bulk_mailing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
