package com.admin_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class BatchDto {
    private String name;
    private Long courseId;
    private List<Long> studentIds;
}