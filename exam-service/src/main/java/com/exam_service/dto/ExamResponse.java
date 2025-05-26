package com.exam_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponse {
    private Long id;
    private String title;
    private LocalDateTime scheduledAt;
    private Long courseId;
}
