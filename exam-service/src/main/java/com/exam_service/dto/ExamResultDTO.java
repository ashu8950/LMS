package com.exam_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResultDTO {
    private Long id;
    private Long examId;
    private Long studentId;
    private Double score;
    private String grade;
}
