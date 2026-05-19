package com.example.mauswag.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "labor_rate_log")
@Data
public class LaborRateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rate_id;

    private Integer emp_id;
    private BigDecimal hourly_rate;
    private LocalDate effective_date;
    private Integer changed_by_admin_id;
}
