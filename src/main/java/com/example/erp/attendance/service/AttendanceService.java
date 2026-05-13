
package com.example.erp.attendance.service;

import com.example.erp.attendance.dto.AttendanceResponse;
import com.example.erp.attendance.entity.Attendance;
import com.example.erp.attendance.repository.AttendanceRepository;
import com.example.erp.user.entity.User;
import com.example.erp.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // CHECK-IN
    // =========================
    public Attendance markAttendance(String email) {

        User user = userRepository.findByEmail(email);
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByUser_IdAndDate(user.getId(), today)
                .orElse(new Attendance());

        if (attendance.getCheckInTime() != null) {
            throw new RuntimeException("Already checked in today");
        }

        attendance.setUser(user);
        attendance.setDate(today);
        attendance.setCheckInTime(LocalDateTime.now());

        LocalTime now = LocalTime.now();

        attendance.setStatus(
                now.isAfter(LocalTime.of(9, 15)) ? "Late" : "Present"
        );

        return attendanceRepository.save(attendance);
    }

    // =========================
    // CHECK-OUT
    // =========================
    public Attendance checkOut(String email) {

        User user = userRepository.findByEmail(email);
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByUser_IdAndDate(user.getId(), today)
                .orElseThrow(() -> new RuntimeException("Check-in not found"));

        attendance.setCheckOutTime(LocalDateTime.now());

        if (attendance.getCheckInTime() != null) {

            Duration duration = Duration.between(
                    attendance.getCheckInTime(),
                    attendance.getCheckOutTime()
            );

            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();

            attendance.setTotalHours(hours + "h " + minutes + "m");
        }

        return attendanceRepository.save(attendance);
    }

    // =========================
    // EMPLOYEE VIEW
    // =========================
    public List<Attendance> getByEmployee(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return attendanceRepository.findByUser_Id(user.getId());
    }

    // =========================
    // ⭐ TODAY ATTENDANCE (ABSENT FIX)
    // =========================
    public List<AttendanceResponse> getTodayAttendanceFull() {

        LocalDate today = LocalDate.now();

        List<User> employees = userRepository.findByRole("EMPLOYEE");
        List<AttendanceResponse> result = new ArrayList<>();

        for (User user : employees) {

            Attendance attendance = attendanceRepository
                    .findByUser_IdAndDate(user.getId(), today)
                    .orElse(null);

            if (attendance == null) {

                AttendanceResponse dto = new AttendanceResponse();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setDate(today);
                dto.setStatus("Absent");

                result.add(dto);

            } else {
                result.add(AttendanceResponse.from(attendance));
            }
        }

        return result;
    }

    // =========================
    // ALL ATTENDANCE
    // =========================
    public List<Attendance> getAll() {
        return attendanceRepository.findAll();
    }

    // =========================
    // RAW TODAY
    // =========================
    public List<Attendance> getTodayAttendance() {
        return attendanceRepository.findByDate(LocalDate.now());
    }

    // =========================
    // MONTHLY ATTENDANCE
    // =========================
    public List<AttendanceResponse> getMonthlyAttendance(String email,
                                                         LocalDate startDate,
                                                         LocalDate endDate) {

        List<Attendance> records =
                attendanceRepository.findByUser_EmailAndDateBetween(
                        email, startDate, endDate
                );

        List<AttendanceResponse> result = new ArrayList<>();

        for (Attendance a : records) {
            result.add(AttendanceResponse.from(a));
        }

        return result;
    }

    // =========================
    // MONTHLY RATE
    // =========================
    public double getMonthlyAttendanceRate(String email,
                                           LocalDate startDate,
                                           LocalDate endDate) {

        List<Attendance> records =
                attendanceRepository.findByUser_EmailAndDateBetween(
                        email, startDate, endDate
                );

        if (records.isEmpty()) return 0.0;

        long presentCount = records.stream()
                .filter(a -> a.getStatus() != null &&
                        (a.getStatus().equalsIgnoreCase("Present")
                                || a.getStatus().equalsIgnoreCase("Late")))
                .count();

        return (presentCount * 100.0) / records.size();
    }
}