package com.exam_service.controllers;




import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.exam_service.dto.ExamRequest;
import com.exam_service.dto.ExamResponse;
import com.exam_service.dto.ExamResultDTO;
import com.exam_service.feign.NotificationClient;
import com.exam_service.service.ExamResultService;
import com.exam_service.service.ExamService;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final ExamResultService resultService;
    private final NotificationClient notifier;

    @PostMapping
    public ResponseEntity<ExamResponse> schedule(@RequestBody ExamRequest req) {
        return ResponseEntity.ok(examService.scheduleExam(req));
    }

    @GetMapping
    public ResponseEntity<List<ExamResponse>> list() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getExamById(id));
    }

    @PostMapping("/{examId}/results")
    public ResponseEntity<ExamResultDTO> submitResult(
            @PathVariable Long examId,
            @RequestBody ExamResultDTO dto) {

        dto.setExamId(examId);
        ExamResultDTO saved = resultService.submitResult(dto);
        // notify student
        notifier.sendExamResultNotification(saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{examId}/results")
    public ResponseEntity<List<ExamResultDTO>> results(@PathVariable Long examId) {
        return ResponseEntity.ok(resultService.getResultsByExam(examId));
    }
}
