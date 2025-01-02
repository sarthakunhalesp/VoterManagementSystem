package com.example.demo.entity;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class MasterUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long master_id;
    
    @Column(name = "master_username")
    private String username;
    
    @Column(name = "master_password")
    private String password;
    
    @Column(name = "master_email")
    private String email;

    @OneToMany(mappedBy = "master")
    private List<User> users;
    
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_created_at")
    private LocalDateTime tokenCreatedAt;

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;
    
    //set and get

	public Long getMaster_id() {
		return master_id;
	}

	public void setMaster_id(Long master_id) {
		this.master_id = master_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
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
    
    
}
