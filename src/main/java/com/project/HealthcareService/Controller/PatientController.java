package com.project.HealthcareService.Controller;

import com.project.HealthcareService.DTOs.request.UpdatePatientProfileRequest;
import com.project.HealthcareService.DTOs.response.AppointmentResponse;
import com.project.HealthcareService.DTOs.response.PatientProfileResponse;
import com.project.HealthcareService.Service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_PATIENT')")
public class PatientController {
    private final PatientService patientservice;

    @GetMapping("/myProfile")
    public ResponseEntity<Optional<PatientProfileResponse>> getProfile(Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(patientservice.getProfileByEmail(email));
    }

    @PutMapping("/updateMyProfile")
    public ResponseEntity<Optional<PatientProfileResponse>> updateProfile(
            @RequestBody UpdatePatientProfileRequest request,
            Authentication authentication){
        String email=authentication.getName();
        return ResponseEntity.ok(patientservice.updateProfile(email,request));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(Authentication authentication){
        String email=authentication.getName();
        System.out.println(email+"is email");
        return ResponseEntity.ok(patientservice.getMyAppointments(email));
    }
}
