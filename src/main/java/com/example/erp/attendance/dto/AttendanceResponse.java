package com.example.erp.attendance.dto;

import com.example.erp.attendance.entity.Attendance;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String totalHours;
    private String status;

    public static AttendanceResponse from(Attendance a) {
        AttendanceResponse dto = new AttendanceResponse();

        dto.setId(a.getId());
        dto.setName(a.getUser().getName());
        dto.setEmail(a.getUser().getEmail());
        dto.setDate(a.getDate());
        dto.setCheckInTime(a.getCheckInTime());
        dto.setCheckOutTime(a.getCheckOutTime());
        dto.setTotalHours(a.getTotalHours());
        dto.setStatus(a.getStatus());

        return dto;
    }
}