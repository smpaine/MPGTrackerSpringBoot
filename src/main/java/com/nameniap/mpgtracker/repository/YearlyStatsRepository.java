package com.nameniap.mpgtracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.nameniap.mpgtracker.model.YearlyStats;
import com.nameniap.mpgtracker.model.YearlyStatsId;

public interface YearlyStatsRepository extends JpaRepository<YearlyStats, YearlyStatsId> {
	List<YearlyStats> findByVidOrderByYear(@Param("vid") int vid);
	List<YearlyStats> findByVidOrderByYearDesc(@Param("vid") int vid);
    Optional<YearlyStats> findByYearlyStatsId(@Param("id") YearlyStatsId id);
}
