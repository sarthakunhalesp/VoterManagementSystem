package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.entity.MasterUser;
import com.example.demo.entity.User;
import com.example.demo.exceptionHandler.ValidationException;
import com.example.demo.service.AuthService;
import com.example.demo.service.MasterUserService;
import com.example.demo.service.UserService;
import com.example.demo.service.ValidationService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api")
public class AuthController {
   
	
	  @Autowired
	  private AuthService authService;
	  
	  @Autowired
	  private UserService userService;

	  @Autowired
	  private MasterUserService masterUserService;
	  
	  @Autowired
	  private ValidationService validationService;
	  
	  
	    // Master login API
	    @PostMapping("/master-login")
	    public ResponseEntity<String> masterLogin(@RequestBody LoginRequest loginRequest) {
	        try {
	        	validationService.validatePasswordPolicy(loginRequest.getPassword());
	            MasterUser masterUser = masterUserService.findByUsername(loginRequest.getUsername());
	            if (masterUser != null && masterUser.getPassword().equals(loginRequest.getPassword())) {
	                return ResponseEntity.ok("Master login successful");
	            }
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	        } catch (ValidationException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
	        }
	    }
	    
	    
	    // User login API
	    @PostMapping("/user-login")
	    public ResponseEntity<String> userLogin(@RequestBody LoginRequest loginRequest) {
	        try {
	        	validationService.validatePasswordPolicy(loginRequest.getPassword());
	            User user = userService.findByUsername(loginRequest.getUsername());
	            if (user == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	            }
	            if (user.getPassword().equals(loginRequest.getPassword())) {
	                return ResponseEntity.ok("User login successful");
	            }
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	        } catch (ValidationException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
	        }
	    }
	  
	  
	    // Forgot Username API
	    @PostMapping("/forgot-username")
	    public ResponseEntity<String> forgotUsername(@Valid @RequestBody ForgotPasswordRequest request) {
	        try {
	        	validationService.validateEmail(request.getEmail());
	            authService.processForgotUsername(request.getEmail());
	            return ResponseEntity.ok("If an account exists with the provided email, the username has been sent to your email.");
	        } catch (ValidationException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
	        }
	    }
	  
	    
	    // Forgot Password API
	    @PostMapping("/forgot-password")
	    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
	        try {
	        	validationService.validateEmail(request.getEmail());
	            authService.processForgotPassword(request.getEmail());
	            return ResponseEntity.ok("If an account exists with the provided email, an OTP has been sent to your email.");
	        } catch (ValidationException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
	        }
	    }

	    
	    // Reset Password API
	    @PostMapping("/reset-password")
	    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
	        try {
	        	validationService.validatePasswordPolicy(request.getNewPassword());
	            authService.processResetPassword(request.getToken(), request.getNewPassword());
	            return ResponseEntity.ok("Your password has been successfully reset. Kindly login again...");
	        } catch (ValidationException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while resetting the password");
	        }
	    }
}
