package com.instructor_service.exception;

public class InstructorNotFoundException extends RuntimeException {
    
    public InstructorNotFoundException(String message) {
        super(message);
    }
}
