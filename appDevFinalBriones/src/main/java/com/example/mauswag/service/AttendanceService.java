package com.example.mauswag.service;

import com.example.mauswag.model.Attendance;
import com.example.mauswag.model.Employee;
import com.example.mauswag.repository.AttendanceRepository;
import com.example.mauswag.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public void importAttendanceFromCSV(MultipartFile file) throws Exception {
        List<Attendance> attendanceList = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                String[] data = line.split(",");
                if (data.length < 4) continue;
                String empName = data[0].trim();
                String dateStr = data[1].trim();
                String timeInStr = data[2].trim();
                String timeOutStr = data[3].trim();

                Employee emp = employeeRepository.findByEmp_name(empName)
                        .orElseThrow(() -> new RuntimeException("Employee not found: " + empName));

                LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                LocalTime timeIn = LocalTime.parse(timeInStr, timeFormatter);
                LocalTime timeOut = LocalTime.parse(timeOutStr, timeFormatter);

                Attendance attendance = new Attendance();
                attendance.setEmp_id(emp.getEmp_id());
                attendance.setAttendance_date(date);
                attendance.setTime_in(LocalDateTime.of(date, timeIn));
                attendance.setTime_out(LocalDateTime.of(date, timeOut));
                attendanceList.add(attendance);
            }
            attendanceRepository.saveAll(attendanceList);
        }
    }

    public List<Attendance> getFilteredAttendance(String start, String end, String search) {
        List<Attendance> allRecords = attendanceRepository.findAll();

        if (start != null && !start.isEmpty() && end != null && !end.isEmpty()) {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            allRecords = allRecords.stream()
                .filter(a -> a.getAttendance_date() != null)
                .filter(a -> !a.getAttendance_date().isBefore(startDate) && !a.getAttendance_date().isAfter(endDate))
                .collect(Collectors.toList());
        }

        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            allRecords = allRecords.stream()
                .filter(a -> {
                    Employee emp = employeeRepository.findById(a.getEmp_id()).orElse(null);
                    return emp != null && emp.getEmp_name().toLowerCase().contains(searchLower);
                })
                .collect(Collectors.toList());
        }

        allRecords.forEach(a -> {
            employeeRepository.findById(a.getEmp_id()).ifPresent(e -> {
                a.setEmp_name(e.getEmp_name());
                a.setEmp_role(e.getEmp_role());
            });

            if (a.getTime_in() != null && a.getTime_out() != null) {
                Duration duration = Duration.between(a.getTime_in(), a.getTime_out());
                
                double rawHours = duration.toMinutes() / 60.0;
                if (rawHours < 0) {
                    rawHours += 24.0;
                }
                
                // Lunch deduction rule
                if (rawHours > 5.0) {
                    rawHours -= 1.0;
                }
                
                double total = Math.round(rawHours * 100.0) / 100.0;
                double reg = Math.min(total, 8.0);
                double ot = Math.max(0.0, total - 8.0);
                
                reg = Math.round(reg * 100.0) / 100.0;
                ot = Math.round(ot * 100.0) / 100.0;

                a.setTotal_hours(total);
                a.setReg_hours(reg);
                a.setOt_hours(ot);
            } else {
                a.setTotal_hours(0.0);
                a.setReg_hours(0.0);
                a.setOt_hours(0.0);
            }
        });

        return allRecords;
    }

    public void saveSingleRecord(Map<String, Object> data) {
        String empName = (String) data.get("name");
        String dateStr = (String) data.get("date");
        String timeInStr = (String) data.get("tIn");
        String timeOutStr = (String) data.get("tOut");

        Employee emp = employeeRepository.findByEmp_name(empName)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + empName));

        LocalDate date = LocalDate.parse(dateStr);
        
        // Fast lookup validation as instructed
        boolean exists = attendanceRepository.findByEmpIdAndAttendanceDate(emp.getEmp_id(), date).isPresent();
        if (exists) {
            throw new RuntimeException("Duplicate record exists for " + empName + " on " + dateStr);
        }

        LocalTime timeIn = LocalTime.parse(timeInStr);
        LocalTime timeOut = LocalTime.parse(timeOutStr);
        
        Attendance attendance = new Attendance();
        attendance.setEmp_id(emp.getEmp_id());
        attendance.setAttendance_date(date);
        attendance.setTime_in(LocalDateTime.of(date, timeIn));
        attendance.setTime_out(LocalDateTime.of(date, timeOut));

        attendanceRepository.save(attendance);
    }

    public void saveManualBatch(List<Map<String, Object>> attendanceData) {
        for (Map<String, Object> data : attendanceData) {
            try {
                saveSingleRecord(data);
            } catch (Exception e) {
                // Ignore failure for existing duplicates during batch
            }
        }
    }
    
    public List<Attendance> getAllAttendance() {
        return getFilteredAttendance(null, null, null);
    }
}
