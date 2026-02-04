package com.project.HealthcareService.Service.impl;

import com.project.HealthcareService.DTOs.request.CreateAppointmentRequest;
import com.project.HealthcareService.DTOs.response.AppointmentResponse;
import com.project.HealthcareService.Model.Appointment;
import com.project.HealthcareService.Model.AppointmentStatus;
import com.project.HealthcareService.Repository.AppointmentRepository;
import com.project.HealthcareService.Service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @Override
    public AppointmentResponse createAppointment(String email , CreateAppointmentRequest request) {
        Appointment appointment = Appointment.builder()
                .patientEmail(email )
                .doctorId(request.getDoctorId())
                .dateTime(request.getDateTime())
                .reason(request.getReason())
                .status(AppointmentStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentResponse.builder()
                .id(saved.getId())
                .patientEmail(String.valueOf(saved.getPatientEmail()))
                .doctorId(String.valueOf(saved.getDoctorId()))
                .dateTime(saved.getDateTime())
                .reason(saved.getReason())
                .status(saved.getStatus())
                .build();
    }
    @Override
    public List<AppointmentResponse> getMyAppointments(String patientEmail) {
        return appointmentRepository.findByPatientEmail(patientEmail)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientEmail(appointment.getPatientEmail())
                .doctorId(appointment.getDoctorId())
                .dateTime(appointment.getDateTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .build();
    }
    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        return mapToResponse(updated);
    }
}

