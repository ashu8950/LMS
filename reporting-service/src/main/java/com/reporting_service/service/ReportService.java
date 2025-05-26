package com.reporting_service.service;

import org.springframework.stereotype.Service;

import com.reporting_service.dto.BatchDto;
import com.reporting_service.dto.ExamResultDto;
import com.reporting_service.dto.PaymentDto;
import com.reporting_service.dto.ReportResponse;
import com.reporting_service.dto.StudentDto;
import com.reporting_service.feign.BatchClient;
import com.reporting_service.feign.ExamClient;
import com.reporting_service.feign.PaymentClient;
import com.reporting_service.feign.StudentClient;

import java.util.List;

@Service
public class ReportService {

    private final StudentClient studentClient;
    private final ExamClient examClient;
    private final PaymentClient paymentClient;
    private final BatchClient batchClient;

    public ReportService(StudentClient studentClient,
                         ExamClient examClient,
                         PaymentClient paymentClient,
                         BatchClient batchClient) {
        this.studentClient = studentClient;
        this.examClient = examClient;
        this.paymentClient = paymentClient;
        this.batchClient = batchClient;
    }

    public ReportResponse<StudentDto> getStudentReport(Long studentId) {
        StudentDto student = studentClient.getStudent(studentId);
        List<ExamResultDto> results = examClient.getResultsForStudent(studentId);
        return new ReportResponse<>(student, results);
    }

    public ReportResponse<BatchDto> getBatchReport(Long batchId) {
        BatchDto batch = batchClient.getBatch(batchId);
        // For simplicity, reuse ExamClient to fetch all results per batch?
        // Or you could add a dedicated endpoint.
        List<ExamResultDto> results = examClient.getResultsForStudent(batchId);
        return new ReportResponse<>(batch, results);
    }

    public ReportResponse<PaymentDto> getPaymentReport(Long userId) {
        // userId is the same as student/admin/instructor id
        List<PaymentDto> payments = paymentClient.getPaymentsForUser(userId);
        return new ReportResponse<>(null, payments);
    }
}
