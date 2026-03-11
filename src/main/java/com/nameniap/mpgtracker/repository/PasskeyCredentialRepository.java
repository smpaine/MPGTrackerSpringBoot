package com.nameniap.mpgtracker.repository;

import com.nameniap.mpgtracker.model.PasskeyCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasskeyCredentialRepository extends JpaRepository<PasskeyCredential, Integer> {
    List<PasskeyCredential> findByUserId(Integer userId);
    Optional<PasskeyCredential> findByCredentialId(String credentialId);
}
