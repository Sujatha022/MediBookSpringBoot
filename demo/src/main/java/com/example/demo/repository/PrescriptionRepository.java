package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Prescription;
import com.example.demo.model.Appointment;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Prescription findByAppointment(Appointment appointment);
}
