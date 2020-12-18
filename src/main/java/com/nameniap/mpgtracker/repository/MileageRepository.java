package com.nameniap.mpgtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nameniap.mpgtracker.model.Mileage;
import com.nameniap.mpgtracker.model.User;

public interface MileageRepository extends Repository<User, Integer> {

    @Query("SELECT t FROM Mileage t where t.id = :id")
    @Transactional(readOnly = true)
    Mileage findById(@Param("id") Integer id);
    
    @Query("SELECT t FROM Mileage t where t.vid = :vid order by t.timestamp asc")
    @Transactional(readOnly = true)
    List<Mileage> findByVehicleId(@Param("vid") Integer vid);
    
    @Query("SELECT t FROM Mileage t order by t.timestamp")
    @Transactional(readOnly = true)
    List<Mileage> getAllMileages();

    void save(Mileage mileage);
}
