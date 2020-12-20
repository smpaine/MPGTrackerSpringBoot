package com.nameniap.mpgtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nameniap.mpgtracker.model.User;

public interface UserRepository extends Repository<User, Integer> {

    @Query("SELECT t FROM User t where t.userName = :userName")
    @Transactional(readOnly = true)
    User findUser(@Param("userName") String userName);

    @Query("SELECT t FROM User t where t.id = :id")
    @Transactional(readOnly = true)
    User findById(@Param("id") Integer id);
    
    @Query("SELECT u FROM User u")
    @Transactional(readOnly = true)
    List<User> getAllUsers();

    void save(User user);
    
    void delete(User user);
}
