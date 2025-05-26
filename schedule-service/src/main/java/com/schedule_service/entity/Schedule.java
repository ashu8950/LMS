package com.schedule_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;
    private Long batchId;
    private Long instructorId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String mode; // ONLINE or OFFLINE
    private String location;
}
