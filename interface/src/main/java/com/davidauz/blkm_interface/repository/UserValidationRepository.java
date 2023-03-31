package com.davidauz.blkm_interface.repository;


import com.davidauz.blkm_interface.entity.UserValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface UserValidationRepository extends JpaRepository<UserValidation, Long> {
    Optional<UserValidation> findByToken(String token);

    Optional<UserValidation> findByPasswordResetToken(String token);
}
