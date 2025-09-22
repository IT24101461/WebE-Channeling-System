package com.webechannelingsystem.web_echannelingsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String contactNumber;
    private String password;

    // Optional: Add fields like age, gender, NIC, etc.

    // Constructors
    public Patient() {}
    public Patient(String fullName, String email, String contactNumber, String password) {
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
    }

    // Getters and setters
}
