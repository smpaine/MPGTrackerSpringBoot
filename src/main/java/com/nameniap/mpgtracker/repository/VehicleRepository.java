package com.nameniap.mpgtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.nameniap.mpgtracker.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Vehicle findByNameOrderBySortkey(@Param("name") String name);
}
