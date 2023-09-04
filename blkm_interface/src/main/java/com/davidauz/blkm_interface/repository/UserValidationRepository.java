package com.davidauz.blkm_interface.repository;


import com.davidauz.blkm_interface.entity.UserValidation;
import com.davidauz.blkm_interface.entity.UserValidation;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface UserValidationRepository extends JpaRepository<UserValidation, Long> {

    Optional<UserValidation> findByUser(Long id);

    Optional<UserValidation> findByTokenAndTokenType(String token, long tokenType);
}
