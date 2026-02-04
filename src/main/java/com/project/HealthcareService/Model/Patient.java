package com.project.HealthcareService.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    private String userId;
    @OneToOne
    @MapsId
    @JoinColumn(name="user_id")
    private ApplicationUser user;
    private String userName;
    private int age;
    private String gender;
    private String contact;
}
