package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PrescriptionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PatientController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    
    // VIEW PRESCRIPTION
   
    @GetMapping("/view-prescription")
    public String viewPrescription(
            @RequestParam Long appointmentId,
            HttpSession session,
            Model model) {

        Patient patient = (Patient) session.getAttribute("patient");
        if (patient == null) {
            return "redirect:/login";
        }

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            return "redirect:/my-appointments";
        }

        Prescription prescription =
                prescriptionRepository.findByAppointment(appointment);

        model.addAttribute("prescription", prescription);
        return "view-prescription";
    }
}
