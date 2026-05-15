package com.project.HealthcareService.Service.impl;

import com.project.HealthcareService.DTOs.request.ChangePasswordRequest;
import com.project.HealthcareService.DTOs.request.UpdatePatientProfileRequest;
import com.project.HealthcareService.DTOs.response.AppointmentResponse;
import com.project.HealthcareService.DTOs.response.PatientProfileResponse;
import com.project.HealthcareService.Model.ApplicationUser;
import com.project.HealthcareService.Model.Appointment;
import com.project.HealthcareService.Model.Doctor;
import com.project.HealthcareService.Model.Patient;
import com.project.HealthcareService.Repository.ApplicationUserRepository;
import com.project.HealthcareService.Repository.AppointmentRepository;
import com.project.HealthcareService.Repository.DoctorRepository;
import com.project.HealthcareService.Repository.PatientRepository;
import com.project.HealthcareService.Service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepo;
    private final ApplicationUserRepository repo;
    private  final AppointmentRepository appointmentRepo;
    private final PasswordEncoder encoder;
    private final DoctorRepository doctorRepo;

    @Override
    public Optional<PatientProfileResponse> getProfileByEmail(String email) {
        ApplicationUser user = repo
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientRepo.findByUser(user)
                .orElseGet(() -> {
                    Patient p = new Patient();
                    p.setUser(user);
//                    p.setUserId(user.getUserId());
                    return patientRepo.save(p);
                });

        return mapToResponse(patient);
    }
    @Override
    public Optional<PatientProfileResponse> updateProfile(String email, UpdatePatientProfileRequest request) {
        Patient patient=patientRepo.findByUser_Email(email)
                .orElseThrow(()->new RuntimeException("Patient profile not found"));

        patient.setUserName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setContact(request.getContact());
        patientRepo.save(patient);
        return mapToResponse(patient);
    }
    private Optional<PatientProfileResponse> mapToResponse(Patient patient) {
        return Optional.ofNullable(PatientProfileResponse.builder()
                .userId(patient.getUserId())
                .name(patient.getUser().getUserName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .contact(patient.getContact())
                .email(patient.getUser().getEmail())
                .build());
    }
    @Override
    public List<AppointmentResponse> getMyAppointments(String email) {
        return appointmentRepo.findByPatientEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    private AppointmentResponse mapToResponse(Appointment appointment) {
        String doctorName = doctorRepo.findById(appointment.getDoctorId())
                .map(Doctor::getUserName)   // assuming field is userName
                .orElse("Unknown Doctor");

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientEmail(appointment.getPatientEmail())
                .doctorName(doctorName)
                .doctorId(appointment.getDoctorId())
                .dateTime(appointment.getDateTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .build();
    }
    @Override
    public AppointmentResponse cancelAppointment(Long appointmentId, String email) {
        return null;
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        ApplicationUser user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        repo.save(user);
    }


}
