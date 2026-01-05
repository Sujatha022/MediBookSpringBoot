package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Appointment;
import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Prescription;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PrescriptionRepository;

@Controller
public class DoctorController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    // DOCTOR DASHBOARD
   
    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(Model model) {
        model.addAttribute("appointments", appointmentRepository.findAll());
        return "doctor-dashboard";
    }

   
    
    // OPEN PRESCRIPTION PAGE
    
    @GetMapping("/doctor/prescribe")
    public String prescribePage(
            @RequestParam Long appointmentId,
            Model model) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId).orElseThrow();

        model.addAttribute("appointment", appointment);
        return "prescribe";
    }
   
    

    
    @PostMapping("/doctor/prescribe")
    public String savePrescription(
            @RequestParam Long appointmentId,
            @RequestParam String diagnosis,
            @RequestParam String medicines,
            @RequestParam String notes) {

       Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDiagnosis(diagnosis);
        prescription.setMedicines(medicines);

       
        prescription.setDoctorNotes(notes);

       
        prescription.setPrescribedDate(LocalDate.now());

        prescriptionRepository.save(prescription);

        
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return "redirect:/doctor/dashboard";
    }
}
