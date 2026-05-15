package com.project.HealthcareService.Controller;

import com.project.HealthcareService.DTOs.request.ChangePasswordRequest;
import com.project.HealthcareService.DTOs.request.UpdatePatientProfileRequest;
import com.project.HealthcareService.DTOs.response.AppointmentResponse;
import com.project.HealthcareService.DTOs.response.DoctorProfileResponse;
import com.project.HealthcareService.DTOs.response.PatientProfileResponse;
import com.project.HealthcareService.Model.Doctor;
import com.project.HealthcareService.Service.DoctorService;
import com.project.HealthcareService.Service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private  final DoctorService doctorService;

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

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request, Authentication authentication){
        try{
            patientservice.changePassword(authentication.getName(), request);
            return ResponseEntity.ok("Password changed successfully");
        }catch(RuntimeException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/allDoctors")
    public ResponseEntity<List<DoctorProfileResponse>> getDoctorsList(){
        System.out.println("are we getting the list of doctors");
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
}
