package com.project.HealthcareService.Service;

import com.project.HealthcareService.Model.ApplicationUser;

import java.util.List;

public interface ApplicationUserService {
    boolean register(ApplicationUser user);
    String login(String email,String password);
     List<ApplicationUser> getAllUsers();
     boolean deleteUser(String userId);
}
