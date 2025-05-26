package com.reporting_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reporting_service.dto.BatchDto;
import com.reporting_service.dto.PaymentDto;
import com.reporting_service.dto.ReportResponse;
import com.reporting_service.dto.StudentDto;
import com.reporting_service.service.ReportService;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<ReportResponse<StudentDto>> studentReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getStudentReport(id));
    }

    @GetMapping("/batch/{id}")
    public ResponseEntity<ReportResponse<BatchDto>> batchReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getBatchReport(id));
    }

    @GetMapping("/payment/{userId}")
    public ResponseEntity<ReportResponse<PaymentDto>> paymentReport(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getPaymentReport(userId));
    }
}
