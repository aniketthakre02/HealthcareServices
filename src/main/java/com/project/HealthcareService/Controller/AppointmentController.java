package com.project.HealthcareService.Controller;

import com.project.HealthcareService.DTOs.request.CreateAppointmentRequest;
import com.project.HealthcareService.DTOs.response.AppointmentResponse;
import com.project.HealthcareService.Model.ApplicationUser;
import com.project.HealthcareService.Service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request,
            Authentication authentication) {
        String email  = authentication.getName(); // from JWT
        AppointmentResponse response= appointmentService.createAppointment(email, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/myappointments")
    @PreAuthorize("hasRole('PATIENT')")
    public List<AppointmentResponse> getMyAppointments(Authentication authentication){
//        ApplicationUser user =(ApplicationUser)authentication.getPrincipal();
        String patientEmail = authentication.getName();
        return appointmentService.getMyAppointments(patientEmail);
    }
}
