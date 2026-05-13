package com.example.erp.sales.controller;

import com.example.erp.sales.entity.SalesOrder;
import com.example.erp.sales.repository.SalesOrderRepository;

import com.example.erp.finance.entity.Revenue;
import com.example.erp.finance.repository.RevenueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:3000")
public class SalesController {

    @Autowired
    private SalesOrderRepository salesRepo;

    @Autowired
    private RevenueRepository revenueRepo;

    // Get all orders
    @GetMapping("/orders")
    public List<SalesOrder> getOrders() {
        return salesRepo.findAll();
    }

    // Save Order + Auto Revenue Entry
    @PostMapping("/orders")
    public SalesOrder saveOrder(@RequestBody SalesOrder order) {

        order.setOrderDate(LocalDate.now());

        SalesOrder saved = salesRepo.save(order);

        // Auto add finance revenue
        Revenue revenue = new Revenue();
        revenue.setSource("Order #" + saved.getId());
        revenue.setAmount(saved.getTotalAmount());
        revenue.setDate(LocalDate.now());
        revenue.setPaymentType(saved.getPaymentType());
        revenue.setStatus("Received");

        revenueRepo.save(revenue);

        return saved;
    }
}