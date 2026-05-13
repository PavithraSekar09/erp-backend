package com.example.erp.leave.controller;

import com.example.erp.leave.entity.Leave;
import com.example.erp.leave.service.LeaveService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/leave")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveController {

    private final LeaveService service;

    public LeaveController(LeaveService service) {
        this.service = service;
    }

    // ================= APPLY LEAVE =================
    @PostMapping
    public Leave apply(@RequestBody Leave leave) {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        leave.setEmployeeEmail(email);
        leave.setStatus("PENDING");

        return service.applyLeave(leave);
    }

    // ================= GET ALL LEAVES (ADMIN) =================
    @GetMapping
    public List<Leave> getAll() {
        return service.getAll();
    }

    // ================= GET OWN LEAVES =================
    @GetMapping("/my")
    public List<Leave> getMyLeaves() {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return service.getByEmail(email);
    }

    // ================= COUNT =================
    @GetMapping("/count")
    public Map<String, Long> getLeaveCount() {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        List<Leave> leaves = service.getByEmail(email);

        long total = leaves.size();

        long approved = leaves.stream()
                .filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus()))
                .count();

        long pending = leaves.stream()
                .filter(l -> "PENDING".equalsIgnoreCase(l.getStatus()))
                .count();

        Map<String, Long> result = new HashMap<>();
        result.put("total", total);
        result.put("approved", approved);
        result.put("pending", pending);

        return result;
    }

    // ================= APPROVE LEAVE =================
    @PutMapping("/approve/{id}")
    public Leave approve(@PathVariable Long id) {
        return service.approve(id);
    }

    // ================= REJECT LEAVE =================
    @PutMapping("/reject/{id}")
    public Leave reject(@PathVariable Long id) {
        return service.reject(id);
    }
}