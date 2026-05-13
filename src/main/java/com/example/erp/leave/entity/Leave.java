package com.example.erp.leave.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_request")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeEmail;
    private String reason;
    private String status; // PENDING / APPROVED / REJECTED

    public Leave() {
        this.status = "PENDING"; // default
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}