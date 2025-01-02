package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(name = "user_username")
    private String username;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;
    
    @ManyToOne
    @JoinColumn(name = "master_id")
    private MasterUser master;


    // Reset token and expiration
    @Column(name = "reset_token")
    private String resetToken;
    
    @Column(name = "token_created_at")
    private LocalDateTime tokenCreatedAt;
    
    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;


    // Getters and Setters
    
	public String getUsername() {
		return username;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public LocalDateTime getTokenCreatedAt() {
		return tokenCreatedAt;
	}

	public void setTokenCreatedAt(LocalDateTime tokenCreatedAt) {
		this.tokenCreatedAt = tokenCreatedAt;
	}

	public LocalDateTime getTokenExpiresAt() {
		return tokenExpiresAt;
	}

	public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) {
		this.tokenExpiresAt = tokenExpiresAt;
	}

	public MasterUser getMaster() {
		return master;
	}

	public void setMaster(MasterUser master) {
		this.master = master;
	}


    
}



