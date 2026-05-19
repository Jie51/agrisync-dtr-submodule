package com.example.mauswag.controller;

import com.example.mauswag.model.Attendance;
import com.example.mauswag.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/import")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            attendanceService.importAttendanceFromCSV(file);
            return ResponseEntity.ok("Attendance imported successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<Attendance> getAll() {
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/logs")
    public List<Attendance> getAttendanceLogs(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) String search) {
        return attendanceService.getFilteredAttendance(start, end, search);
    }

    @PostMapping("/save-batch")
    public ResponseEntity<?> saveBatch(
            @RequestBody List<Map<String, Object>> attendanceData,
            @RequestHeader(value = "Authorization", required = false) String token) {
            
        if (token == null || !com.example.mauswag.controller.AuthController.activeTokens.containsKey(token)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body("Access Denied: Invalid Security Token.");
        }
        
        try {
            attendanceService.saveManualBatch(attendanceData);
            return ResponseEntity.ok().body("{\"message\": \"Batch saved successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/add-single")
    public ResponseEntity<?> addSingle(@RequestBody Map<String, Object> data) {
        try {
            attendanceService.saveSingleRecord(data);
            return ResponseEntity.ok().body("{\"message\": \"Record added successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
