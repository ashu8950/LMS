package com.admin_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String role;
    private String email;
}