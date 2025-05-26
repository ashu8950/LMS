package com.exam_service.service;



import java.util.List;

import com.exam_service.dto.ExamRequest;
import com.exam_service.dto.ExamResponse;

public interface ExamService {
    ExamResponse scheduleExam(ExamRequest request);
    List<ExamResponse> getAllExams();
    ExamResponse getExamById(Long id);
}
