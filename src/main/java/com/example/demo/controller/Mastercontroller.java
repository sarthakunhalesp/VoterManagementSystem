package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.CreateMasterRequest;
import com.example.demo.dto.CreateUserRequest;
import com.example.demo.entity.MasterUser;
import com.example.demo.entity.User;
import com.example.demo.exceptionHandler.ValidationException;
import com.example.demo.service.MasterUserService;
import com.example.demo.service.UserService;
import com.example.demo.service.ValidationService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/master")
public class Mastercontroller {
	
	  @Autowired
	  private UserService userService;

	  @Autowired
	  private MasterUserService masterUserService;
	  
	  @Autowired
	  private ValidationService validationService;
	  
	
	
	    // Create Master API By Master
	    @PostMapping("/create-master")
	    public ResponseEntity<String> createMaster(@Valid @RequestBody CreateMasterRequest createMasterRequest) {
	        try {
	        	validationService.validatePasswordPolicy(createMasterRequest.getPassword());
	        	validationService.validateEmail(createMasterRequest.getEmail());
	            MasterUser masterUser = masterUserService.createMasterUser(
	                    createMasterRequest.getUsername(),
	                    createMasterRequest.getPassword(),
	                    createMasterRequest.getEmail()
	            );
	            if (masterUser != null) {
	                return ResponseEntity.ok("Master user created successfully");
	            }
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create master user");
	        } catch (ValidationException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the master user");
	        }
	    }

	    // Create User API (by Master)
	    @PostMapping("/create-user")
	    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
	        try {
	        	validationService.validatePasswordPolicy(createUserRequest.getPassword());
	        	validationService.validateEmail(createUserRequest.getEmail());
	            User user = userService.createUser(
	                    createUserRequest.getMasterUsername(),
	                    createUserRequest.getEmail(),
	                    createUserRequest.getUsername(),
	                    createUserRequest.getPassword()
	            );
	            if (user != null) {
	                return ResponseEntity.ok("User created successfully");
	            }
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
	        } catch (ValidationException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the user");
	        }
	    }
	  
	  
	  // Logout for Master
		@PostMapping("/logout")
		public String logoutUser() {
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth != null) {
		        SecurityContextHolder.clearContext();
		    }
		    return "You have been logged out successfully.";
		}
	
}
