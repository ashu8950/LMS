package com.exam_service.service;



import java.util.List;

import com.exam_service.dto.ExamResultDTO;

public interface ExamResultService {
    ExamResultDTO submitResult(ExamResultDTO dto);
    List<ExamResultDTO> getResultsByExam(Long examId);
}
