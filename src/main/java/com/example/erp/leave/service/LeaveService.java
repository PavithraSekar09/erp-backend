package com.example.erp.leave.service;

import com.example.erp.leave.entity.Leave;
import com.example.erp.leave.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository repo;

    public LeaveService(LeaveRepository repo) {
        this.repo = repo;
    }

    // APPLY LEAVE
    public Leave applyLeave(Leave leave) {
        leave.setStatus("PENDING");
        return repo.save(leave);
    }

    // GET ALL LEAVES (ADMIN)
    public List<Leave> getAll() {
        return repo.findAll();
    }

    // GET BY EMPLOYEE EMAIL
    public List<Leave> getByEmail(String email) {
        return repo.findByEmployeeEmail(email);
    }

    // APPROVE LEAVE
    public Leave approve(Long id) {
        Leave l = repo.findById(id).orElseThrow();
        l.setStatus("APPROVED");
        return repo.save(l);
    }

    // REJECT LEAVE
    public Leave reject(Long id) {
        Leave l = repo.findById(id).orElseThrow();
        l.setStatus("REJECTED");
        return repo.save(l);
    }
}