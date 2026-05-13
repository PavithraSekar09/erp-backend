package com.example.erp.attendance.repository;

import com.example.erp.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUser_IdAndDate(Long userId, LocalDate date);

    List<Attendance> findByUser_Id(Long userId);

    List<Attendance> findByDate(LocalDate date);

    // ✅ MONTHLY DATA (IMPORTANT)
    List<Attendance> findByUser_EmailAndDateBetween(
            String email,
            LocalDate startDate,
            LocalDate endDate
    );
}