package com.content_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;

    private String title;

    @Column(length = 1000)
    private String description;

    private String videoUrl;
}
