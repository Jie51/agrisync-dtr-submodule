package com.example.mauswag.repository;

import com.example.mauswag.model.LaborRateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LaborRateLogRepository extends JpaRepository<LaborRateLog, Integer> {
    
    @Query("SELECT l FROM LaborRateLog l WHERE l.emp_id = :empId AND l.effective_date <= :workDate ORDER BY l.effective_date DESC LIMIT 1")
    Optional<LaborRateLog> findEffectiveRate(@Param("empId") Integer empId, @Param("workDate") LocalDate workDate);
}
