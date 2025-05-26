package com.exam_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {
    private String title;
    private LocalDateTime scheduledAt;
    private Long courseId;
}
