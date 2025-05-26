package com.exam_service.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.exam_service.dto.ExamResultDTO;
import com.exam_service.entity.ExamResult;
import com.exam_service.repository.ExamResultRepository;
import com.exam_service.service.ExamResultService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {

    private final ExamResultRepository repo;

    @Override
    public ExamResultDTO submitResult(ExamResultDTO dto) {
        ExamResult er = ExamResult.builder()
                .examId(dto.getExamId())
                .studentId(dto.getStudentId())
                .score(dto.getScore())
                .grade(dto.getGrade())
                .build();
        ExamResult saved = repo.save(er);
        return ExamResultDTO.builder()
                .id(saved.getId())
                .examId(saved.getExamId())
                .studentId(saved.getStudentId())
                .score(saved.getScore())
                .grade(saved.getGrade())
                .build();
    }

    @Override
    public List<ExamResultDTO> getResultsByExam(Long examId) {
        return repo.findByExamId(examId).stream()
                .map(er -> ExamResultDTO.builder()
                        .id(er.getId())
                        .examId(er.getExamId())
                        .studentId(er.getStudentId())
                        .score(er.getScore())
                        .grade(er.getGrade())
                        .build())
                .collect(Collectors.toList());
    }
}
