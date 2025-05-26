package com.schedule_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO {
    private Long id;
    private Long courseId;
    private Long batchId;
    private Long instructorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String mode;
    private String location;
}
