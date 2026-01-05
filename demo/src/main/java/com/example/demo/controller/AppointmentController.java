package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;

/* 🔥 NEW IMPORT */
import com.example.demo.model.AppointmentStatus;

import com.example.demo.repository.AppointmentRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

   
    @GetMapping("/book-appointment")
    public String bookAppointmentPage(HttpSession session) {

        Patient patient = (Patient) session.getAttribute("patient");
        if (patient == null) {
            return "redirect:/login";
        }

        return "book-appointment";
    }

    
    @PostMapping("/book-appointment")
    public String saveAppointment(
            @RequestParam String doctorName,
            @RequestParam LocalDate appointmentDate,
            @RequestParam LocalTime appointmentTime,
            HttpSession session) {

        Patient patient = (Patient) session.getAttribute("patient");
        if (patient == null) {
            return "redirect:/login";
        }

        Appointment appointment = new Appointment();
        appointment.setDoctorName(doctorName);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setPatient(patient);

       
        appointment.setStatus(AppointmentStatus.PENDING);

        appointmentRepository.save(appointment);

        return "appointment-success";
    }

   
    @GetMapping("/my-appointments")
    public String viewAppointments(HttpSession session, Model model) {

        Patient patient = (Patient) session.getAttribute("patient");
        if (patient == null) {
            return "redirect:/login";
        }

        List<Appointment> appointments =
                appointmentRepository.findByPatient(patient);

        model.addAttribute("appointments", appointments);
        return "my-appointments";
    }

    
    @PostMapping("/appointments/cancel")
    public String cancelAppointment(
            @RequestParam Long appointmentId,
            HttpSession session) {

        Patient patient = (Patient) session.getAttribute("patient");
        if (patient == null) {
            return "redirect:/login";
        }

        Appointment appointment =
                appointmentRepository.findById(appointmentId).orElseThrow();

       
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            return "redirect:/my-appointments";
        }

        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        return "redirect:/my-appointments";
    }
}
