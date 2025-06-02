package com.batch_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchDTO {

    private Long id;
    private String name;
    private String description;
    private Long courseId;  
}
