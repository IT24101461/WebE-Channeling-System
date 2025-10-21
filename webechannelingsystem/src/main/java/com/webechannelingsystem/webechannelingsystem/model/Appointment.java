package com.webechannelingsystem.webechannelingsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Appointment_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Patient_ID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "Doctor_ID", nullable = false)
    private Doctor doctor;

    @Column(name = "Appointment_Time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "Email")
    private String email;

    @Column(name = "Payment_Method")
    private String paymentMethod;

    @Column(name = "Payment_Status")
    private String paymentStatus;

    @Column(name = "Type", length = 20, nullable = false)
    private String type; // REGULAR or EMERGENCY

    @Column(name = "Status", length = 20, nullable = false)
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    // Constructors
    public Appointment() {}

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime, String email, String paymentMethod, String paymentStatus, String type, String status) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = appointmentTime;
        this.email = email;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.type = type;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}