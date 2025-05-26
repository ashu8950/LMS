package com.content_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDTO {
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private String videoUrl;
}
