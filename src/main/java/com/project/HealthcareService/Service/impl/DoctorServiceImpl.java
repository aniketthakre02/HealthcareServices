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
import java.util.stream.Collectors;

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
               .availability(doctor.getAvailability())
               .introduction(doctor.getIntroduction())
               .experience(doctor.getExperience())
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
        doctor.setAvailability(request.getAvailability());
        doctor.setExperience(request.getExperience());
        doctor.setIntroduction(request.getIntroduction());
        doctorRepo.save(doctor);
        return mapToResponse(doctor);
    }
//    @Override
//    public List<AppointmentResponse> getMyAppointments(String email){
//        ApplicationUser user=repo.findByEmail(email)
//                .orElseThrow(()->new RuntimeException("User not Found"));
//        if (!user.getRoles().contains(Role.ROLE_DOCTOR)) {
//            throw new RuntimeException("Access denied: not a doctor");
//        }
//        return appointmentRepo.findByDoctorId(user.getUserId());
//    }
@Override
public List<AppointmentResponse> getMyAppointments(String email) {
    ApplicationUser user = repo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not Found"));

    if (!user.getRoles().contains(Role.ROLE_DOCTOR)) {
        throw new RuntimeException("Access denied: not a doctor");
    }

    return appointmentRepo.findByDoctorId(user.getUserId())
            .stream()
            .map(appt -> AppointmentResponse.builder()
                    .id(appt.getId())
                    .patientEmail(appt.getPatientEmail())
                    .doctorId(appt.getDoctorId())
                    .dateTime(appt.getDateTime())
                    .reason(appt.getReason())
                    .status(appt.getStatus())
                    .build()
            )
            .collect(Collectors.toList());
}
    @Override
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        return null;
    }

    @Override
    public List<DoctorProfileResponse> getAllDoctors(){
        return doctorRepo.findAll()
                .stream()
                .map(doctor->DoctorProfileResponse.builder()
                        .userId(doctor.getUserId())
                        .name(doctor.getUserName())
                        .specialization(doctor.getSpecialization())
                        .experience(doctor.getExperience())
                        .availability(doctor.getAvailability())
                        .introduction(doctor.getIntroduction())
                        .age(doctor.getAge())
                        .email(doctor.getUser().getEmail())
                        .gender(doctor.getGender())
                        .contact(doctor.getContact())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
