package com.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StudentControllerDTO {
	private Long id;
    private String username;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
}
