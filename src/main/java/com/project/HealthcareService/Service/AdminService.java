package com.project.HealthcareService.Service;

import com.project.HealthcareService.DTOs.request.AdminUpdateUserRequest;
import com.project.HealthcareService.DTOs.response.UserResponse;
import com.project.HealthcareService.Model.ApplicationUser;
import com.project.HealthcareService.Repository.ApplicationUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ApplicationUserRepository userRepository;
    @Transactional
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponse(
                        u.getUserId(),
                        u.getUserName(),
                        u.getEmail(),
                        u.getRoles()
                ))
                .toList();
    }
    @Transactional
    public UserResponse getUserById(String userId) {
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRoles()
        );
    }
    @Transactional
    public UserResponse updateUser(String userId, AdminUpdateUserRequest request) {
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUserName() != null)
            user.setUserName(request.getUserName());

        if (request.getEmail() != null)
            user.setEmail(request.getEmail());

        if (request.getRoles() != null)
            user.setRoles(request.getRoles());
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRoles()
        );
    }
}