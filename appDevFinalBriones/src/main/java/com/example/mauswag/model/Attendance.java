package com.example.mauswag.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attendance_id;

    private Integer emp_id;
    private LocalDate attendance_date;
    private LocalDateTime time_in;
    private LocalDateTime time_out;

    @Transient
    private String emp_name;

    @Transient
    private String emp_role;

    @Transient
    private Double total_hours;

    @Transient
    private Double reg_hours;

    @Transient
    private Double ot_hours;
}
