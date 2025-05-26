package com.exam_service.service.impl;

import org.springframework.stereotype.Service;

import com.exam_service.dto.ExamRequest;
import com.exam_service.dto.ExamResponse;
import com.exam_service.entity.Exam;
import com.exam_service.repository.ExamRepository;
import com.exam_service.service.ExamService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository repo;

    @Override
    public ExamResponse scheduleExam(ExamRequest request) {
        Exam exam = Exam.builder()
                .title(request.getTitle())
                .scheduledAt(request.getScheduledAt())
                .courseId(request.getCourseId())
                .build();
        Exam saved = repo.save(exam);
        return toDto(saved);
    }

    @Override
    public List<ExamResponse> getAllExams() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(Long id) {
        Exam exam = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found: " + id));
        return toDto(exam);
    }

    private ExamResponse toDto(Exam e) {
        return ExamResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .scheduledAt(e.getScheduledAt())
                .courseId(e.getCourseId())
                .build();
    }
}
