package com.nameniap.mpgtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nameniap.mpgtracker.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT t FROM User t where t.userName = :userName")
    @Transactional(readOnly = true)
    User findUser(@Param("userName") String userName);
    
    @Query("SELECT u FROM User u")
    @Transactional(readOnly = true)
    List<User> findAll();
}
