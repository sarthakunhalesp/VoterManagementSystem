package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.exceptionHandler.ValidationException;

@Service
public class ValidationService {
	
	
    // Method to validate password format
    public void validatePasswordPolicy(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be null or empty");
        }
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit");
        }
        if (!password.matches(".*[@#$%^&+=].*")) {
            throw new ValidationException("Password must contain at least one special character (@#$%^&+=)");
        }
    }
    
    
    // Method to validate email format
    public void validateEmail(String email) throws ValidationException {
        if (email == null || email.isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new ValidationException("Invalid email format");
        }
    }

}
