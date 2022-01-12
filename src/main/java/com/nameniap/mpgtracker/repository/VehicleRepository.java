package com.nameniap.mpgtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nameniap.mpgtracker.model.Vehicle;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {

    @Query("SELECT t FROM Vehicle t where t.name = :name order by t.sortkey")
    @Transactional(readOnly = true)
    Vehicle findVehicle(@Param("name") String name);
    
    @Query("SELECT t FROM Vehicle t order by t.sortkey")
    @Transactional(readOnly = true)
    List<Vehicle> findAll();
}
