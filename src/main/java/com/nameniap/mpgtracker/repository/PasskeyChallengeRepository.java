package com.nameniap.mpgtracker.repository;

import com.nameniap.mpgtracker.model.PasskeyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasskeyChallengeRepository extends JpaRepository<PasskeyChallenge, Integer> {
}
