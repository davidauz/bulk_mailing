package com.davidauz.bulk_mailing.repository;


import com.davidauz.bulk_mailing.entity.UserValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface UserValidationRepository extends JpaRepository<UserValidation, Long> {
    Optional<UserValidation> findByToken(String token);

    Optional<UserValidation> findByPasswordResetToken(String token);
}
