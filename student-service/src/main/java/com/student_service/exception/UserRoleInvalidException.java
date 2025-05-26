package com.student_service.exception;

public class UserRoleInvalidException extends RuntimeException {

    public UserRoleInvalidException(String message) {
        super(message);
    }

    public UserRoleInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
