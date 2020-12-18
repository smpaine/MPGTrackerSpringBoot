package com.nameniap.mpgtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nameniap.mpgtracker.model.User;
import com.nameniap.mpgtracker.model.Vehicle;

public interface VehicleRepository extends Repository<User, Integer> {

    @Query("SELECT t FROM Vehicle t where t.name = :name order by t.sortkey")
    @Transactional(readOnly = true)
    Vehicle findVehicle(@Param("name") String name);

    @Query("SELECT t FROM Vehicle t where t.id = :id order by t.sortkey")
    @Transactional(readOnly = true)
    Vehicle findById(@Param("id") Integer id);
    
    @Query("SELECT t FROM Vehicle t order by t.sortkey")
    @Transactional(readOnly = true)
    List<Vehicle> getAllVehicles();

    void save(Vehicle vehicle);
}
