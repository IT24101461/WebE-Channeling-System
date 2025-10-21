package com.webechannelingsystem.web_echannelingsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_booking")
public class EmergencyBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Emergency_Booking_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Patient_ID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "Doctor_ID", nullable = false)
    private Doctor doctor;

    @Column(name = "Booking_Time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "Requested_Time", nullable = false)
    private LocalDateTime requestedTime;

    @Column(name = "Patient_Email")
    private String patientEmail;

    @Column(name = "Emergency_Type", length = 100)
    private String emergencyType;

    @Column(name = "Status", length = 20, nullable = false)
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED

    @Column(name = "Payment_Status", length = 20)
    private String paymentStatus;

    @Column(name = "Payment_Method", length = 50)
    private String paymentMethod;

    @Column(name = "Priority", length = 20)
    private String priority; // HIGH, MEDIUM, LOW

    @Column(name = "Notes", length = 500)
    private String notes;

    // Constructors
    public EmergencyBooking() {
        this.bookingTime = LocalDateTime.now();
        this.status = "PENDING";
        this.priority = "HIGH";
    }

    public EmergencyBooking(Patient patient, Doctor doctor, LocalDateTime requestedTime,
                            String patientEmail, String emergencyType) {
        this();
        this.patient = patient;
        this.doctor = doctor;
        this.requestedTime = requestedTime;
        this.patientEmail = patientEmail;
        this.emergencyType = emergencyType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDateTime getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}