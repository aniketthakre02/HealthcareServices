package com.project.HealthcareService.Service.impl;

import com.project.HealthcareService.Exception.UserAlreadyExistsException;
import com.project.HealthcareService.Model.ApplicationUser;
import com.project.HealthcareService.Repository.ApplicationUserRepository;
import com.project.HealthcareService.Service.ApplicationUserService;
import com.project.HealthcareService.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static com.project.HealthcareService.Model.Role.*;

@Service
@RequiredArgsConstructor
public class ApplicationUserServiceImpl implements ApplicationUserService {
    private final ApplicationUserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    @Override
    public boolean register(ApplicationUser user) {
        if (repo.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User already exists with email: " + user.getEmail()
            );
        }
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
       user.setPassword(encoder.encode(user.getPassword()));
       user.getRoles().add(ROLE_PATIENT);
       repo.save(user);
       return true;
    }
    @Override
    public String login(String email, String password) {
//        System.out.println("do we come in login");
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password)
        );
        ApplicationUser user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
//        System.out.println("did it authenticate");

//        String role = user.getRoles()
//                .stream()
//                .findFirst()
//                .map(r -> r.name())
//                .orElse("ROLE_PATIENT");
        String role;
        if (user.getRoles().contains(ROLE_ADMIN)) {
            role = "ROLE_ADMIN";
        } else if (user.getRoles().contains(ROLE_DOCTOR)) {
            role = "ROLE_DOCTOR";
        } else {
            role = "ROLE_PATIENT";
        }
        return jwtUtil.generateToken(email,user.getUserName(),role);
    }
    @Override
    public List<ApplicationUser> getAllUsers() {
        return repo.findAll();
    }
    @Override
    public boolean deleteUser(String userId) {
        System.out.println("repo.findById(userId)"+repo.findById(userId));
        if (repo.existsById(userId)) {
            repo.deleteById(userId);
            return true;
        }
        return false;
    }
}
