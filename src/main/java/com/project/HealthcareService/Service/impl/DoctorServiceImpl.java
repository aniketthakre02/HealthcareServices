package com.project.HealthcareService.Service.impl;

import com.project.HealthcareService.DTOs.request.UpdateDoctorProfileRequest;
import com.project.HealthcareService.DTOs.response.AppointmentResponse;
import com.project.HealthcareService.DTOs.response.DoctorProfileResponse;
import com.project.HealthcareService.Model.*;
import com.project.HealthcareService.Repository.ApplicationUserRepository;
import com.project.HealthcareService.Repository.AppointmentRepository;
import com.project.HealthcareService.Repository.DoctorRepository;
import com.project.HealthcareService.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepo;
    private final ApplicationUserRepository repo;
    private final AppointmentRepository appointmentRepo;

    @Override
    public DoctorProfileResponse getMyProfile(String email) {
        ApplicationUser user=repo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not Found"));

        Doctor doctor=doctorRepo.findByUser(user)
                .orElseGet(() -> {
                    Doctor d = new Doctor();
                 d.setUser(user);
                 d.setUserName(user.getUserName());
            return doctorRepo.save(d);
        });
        return mapToResponse(doctor);
    }
    private DoctorProfileResponse mapToResponse(Doctor doctor) {
       return DoctorProfileResponse.builder()
               .userId(doctor.getUserId())
               .name(doctor.getUser().getUserName())
               .email(doctor.getUser().getEmail())
               .age(doctor.getAge())
               .gender(doctor.getGender())
               .specialization(doctor.getSpecialization())
               .contact(doctor.getContact())
               .build();
    }

    @Override
    public DoctorProfileResponse updateMyProfile(String email, UpdateDoctorProfileRequest request){
        ApplicationUser user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        if (doctor.getUserName() == null) {
            doctor.setUserName(user.getUserName());
        }
        doctor.setGender(request.getGender());
        doctor.setAge(request.getAge());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setContact(request.getContact());
        doctorRepo.save(doctor);
        return mapToResponse(doctor);
    }
    @Override
    public List<Appointment> getMyAppointments(String email){
        ApplicationUser user=repo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not Found"));
        if (!user.getRoles().contains(Role.ROLE_DOCTOR)) {
            throw new RuntimeException("Access denied: not a doctor");
        }

        return appointmentRepo.findByDoctorId(user.getUserId());
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        return null;
    }
}
