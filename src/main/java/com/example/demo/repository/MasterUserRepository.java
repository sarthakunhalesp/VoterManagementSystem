package com.example.demo.repository;

import com.example.demo.entity.MasterUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterUserRepository extends JpaRepository<MasterUser, Long> {
    
    MasterUser findByUsername(String username);

    Optional<MasterUser> findByEmail(String email);

    Optional<MasterUser> findByResetToken(String resetToken);
}
