package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.MasterUser;
import com.example.demo.entity.User;
import com.example.demo.repository.MasterUserRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private MasterUserRepository masterUserRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    // Forgot Password Logic
    @Transactional
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        MasterUser masterUser = masterUserRepository.findByEmail(email).orElse(null);

        if (user == null && masterUser == null) {
            throw new RuntimeException("No user or master user found with the provided email.");
        }

        // Generate unique token
        String token = generateUniqueToken();

        if (user != null) {
            user.setResetToken(token);
            user.setTokenCreatedAt(LocalDateTime.now());
            user.setTokenExpiresAt(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
        } else {
            masterUser.setResetToken(token);
            masterUser.setTokenCreatedAt(LocalDateTime.now());
            masterUser.setTokenExpiresAt(LocalDateTime.now().plusHours(1));
            masterUserRepository.save(masterUser);
        }

        // Send Email
        emailService.sendEmail(
                email,
                "Password Reset Request",
                "Your Secret Code is: " + token
        );
    }

    // Reset Password Logic
    @Transactional
    public void processResetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token).orElse(null);
        MasterUser masterUser = masterUserRepository.findByResetToken(token).orElse(null);

        if (user == null && masterUser == null) {
            throw new RuntimeException("Invalid or expired reset token.");
        }

        if (user != null) {
            if (user.getTokenExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Reset token has expired.");
            }
            validateAndSetPassword(user, newPassword);
            user.setResetToken(null);
            user.setTokenCreatedAt(null);
            user.setTokenExpiresAt(null);
            userRepository.save(user);
        } else {
            if (masterUser.getTokenExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Reset token has expired.");
            }
            validateAndSetPassword(masterUser, newPassword);
            masterUser.setResetToken(null);
            masterUser.setTokenCreatedAt(null);
            masterUser.setTokenExpiresAt(null);
            masterUserRepository.save(masterUser);
        }
    }

    private void validateAndSetPassword(Object userOrMasterUser, String newPassword) {

        String encodedPassword = newPassword; 

        if (userOrMasterUser instanceof User) {
            ((User) userOrMasterUser).setPassword(encodedPassword);
        } else if (userOrMasterUser instanceof MasterUser) {
            ((MasterUser) userOrMasterUser).setPassword(encodedPassword);
        }
    }

    // Generate a unique four-digit OTP
    public String generateUniqueToken() {
        String token;
        do {
            token = String.valueOf((int) (Math.random() * 9000) + 1000);
        } while (userRepository.findByResetToken(token).isPresent() ||
                masterUserRepository.findByResetToken(token).isPresent());
        return token;
    }
    
    
    // Forgot Username Logic
    public void processForgotUsername(String email) {
        // Check for User by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Check for MasterUser by email
        Optional<MasterUser> masterUserOptional = masterUserRepository.findByEmail(email);

        // Throw an exception if neither User nor MasterUser is found
        if (userOptional.isEmpty() && masterUserOptional.isEmpty()) {
            throw new RuntimeException("No user or master user found with the provided email.");
        }

        // Send email with the username
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            emailService.sendEmail(
                user.getEmail(),
                "Username Retrieval Request",
                "Your Username is: " + user.getUsername()
            );
        } else if (masterUserOptional.isPresent()) {
            MasterUser masterUser = masterUserOptional.get();
            emailService.sendEmail(
                masterUser.getEmail(),
                "Username Retrieval Request",
                "Your Username is: " + masterUser.getUsername()
            );
        }
    }

}
