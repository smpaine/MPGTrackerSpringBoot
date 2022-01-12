package com.nameniap.mpgtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nameniap.mpgtracker.model.Mileage;

public interface MileageRepository extends CrudRepository<Mileage, Integer> {
    
    @Query("SELECT t FROM Mileage t where t.vid = :vid order by t.timestamp asc")
    @Transactional(readOnly = true)
    List<Mileage> findByVehicleId(@Param("vid") Integer vid);
    
    @Query("SELECT t FROM Mileage t order by t.timestamp")
    @Transactional(readOnly = true)
    List<Mileage> findAll();
}
