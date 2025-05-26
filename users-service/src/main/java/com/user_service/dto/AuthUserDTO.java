package com.user_service.dto;

import lombok.Data;

@Data
public class AuthUserDTO {
  private Long id;
  private String email;
  private String username;
  private String role;
  private Boolean enabled;
}