package com.project.HealthcareService.Controller;

import com.project.HealthcareService.Model.ApplicationUser;
import com.project.HealthcareService.Service.ApplicationUserService;
import com.project.HealthcareService.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApplicationUserController {
    private  final ApplicationUserService userService;
    private final JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ApplicationUser user) {
        try {
            userService.register(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("User registered successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ex.getMessage());
        }
    }
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String,String> req){
        System.out.println("are we here");
        String email=req.get("email");
        String password=req.get("password");
        System.out.println(email+"-"+password);
        String token= userService.login(email,password);
        System.out.println(token);
        Map<String,Object>res=new HashMap<>();
        res.put("token",token);
        return res;
    }
}
