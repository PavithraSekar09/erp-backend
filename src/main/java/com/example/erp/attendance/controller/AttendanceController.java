package com.example.erp.attendance.controller;

import com.example.erp.attendance.dto.AttendanceResponse;
import com.example.erp.attendance.entity.Attendance;
import com.example.erp.attendance.service.AttendanceService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    // =========================
    // CHECK-IN
    // =========================
    @PostMapping("/mark")
    public Attendance checkIn() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        return service.markAttendance(email);
    }

    // =========================
    // CHECK-OUT
    // =========================
    @PostMapping("/checkout")
    public Attendance checkOut() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        return service.checkOut(email);
    }

    // =========================
    // MY ATTENDANCE
    // =========================
    @GetMapping("/my")
    public List<Attendance> myAttendance() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        return service.getByEmployee(email);
    }

    // =========================
    // ⭐ TODAY ATTENDANCE (FIXED - IMPORTANT)
    // =========================
    @GetMapping("/today")
    public List<AttendanceResponse> todayAttendance() {
        return service.getTodayAttendanceFull();
    }

    // =========================
    // MONTHLY ATTENDANCE
    // =========================
    @GetMapping("/month")
    public List<AttendanceResponse> monthAttendance(
            @RequestParam int year,
            @RequestParam int month) {

        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return service.getMonthlyAttendance(email, start, end);
    }

    // =========================
    // MONTHLY RATE
    // =========================
    @GetMapping("/rate/month")
    public Map<String, Double> monthlyRate(
            @RequestParam int year,
            @RequestParam int month) {

        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        double rate = service.getMonthlyAttendanceRate(email, start, end);

        return Map.of("rate", rate);
    }

    // =========================
    // ADMIN ALL RAW
    // =========================
    @GetMapping("/all")
    public List<Attendance> getAll() {
        return service.getAll();
    }
}