package com.webechannelingsystem.webechannelingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Emergency")
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmergencyID")
    private Long id;

    @Column(name = "patient_Full_Name", nullable = false)
    private String patientFullName;

    @Column(name = "patient_Email", nullable = false)
    private String patientEmail;

    @Column(name = "patient_Contact_Number", nullable = false)
    private String patientContactNumber;

    @Column(name = "patient_Age", nullable = false)
    private String patientAge;

    @Column(name = "patient_Gender", nullable = false)
    private String patientGender;

    @Column(name = "patient_Issues", nullable = false)
    private String patientIssues;

    @Column(name = "status")
    private String status;


}



