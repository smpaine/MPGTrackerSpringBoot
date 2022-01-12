package com.nameniap.mpgtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.nameniap.mpgtracker.model.Mileage;

public interface MileageRepository extends JpaRepository<Mileage, Integer> {
    List<Mileage> findByVidOrderByTimestamp(@Param("vid") Integer vid);
}
