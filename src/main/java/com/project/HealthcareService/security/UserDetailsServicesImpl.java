package com.project.HealthcareService.security;


import com.project.HealthcareService.Model.ApplicationUser;
import com.project.HealthcareService.Repository.ApplicationUserRepository;
import com.project.HealthcareService.Service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServicesImpl implements UserDetailsService {
    private  final ApplicationUserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser user=repo.findByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("user not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())   // encoded in DB
                .authorities("ROLE_USER")       // or from DB
                .build();
    }

}
