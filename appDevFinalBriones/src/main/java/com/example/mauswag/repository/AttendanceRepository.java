package com.example.mauswag.repository;

import com.example.mauswag.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    
    @Query("SELECT a FROM Attendance a WHERE a.emp_id = :empId AND a.time_in >= :start AND a.time_out <= :end")
    List<Attendance> findByEmpIdAndDateRange(@Param("empId") Integer empId, 
                                             @Param("start") LocalDateTime start, 
                                             @Param("end") LocalDateTime end);

    @Query("SELECT a FROM Attendance a WHERE a.emp_id = :empId AND a.attendance_date = :attendanceDate")
    Optional<Attendance> findByEmpIdAndAttendanceDate(@Param("empId") Integer empId, @Param("attendanceDate") LocalDate attendanceDate);}
