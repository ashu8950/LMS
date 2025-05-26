package com.student_service.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
}
