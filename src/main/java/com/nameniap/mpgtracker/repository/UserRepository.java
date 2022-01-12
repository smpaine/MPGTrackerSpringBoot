package com.nameniap.mpgtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.nameniap.mpgtracker.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(@Param("userName") String userName);
}
