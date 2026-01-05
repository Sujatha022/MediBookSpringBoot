package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

   
    List<Appointment> findByPatient(Patient patient);

    
    List<Appointment> findByStatus(AppointmentStatus status);
}
