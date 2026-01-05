package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.AppointmentStatus;
import com.example.demo.repository.AppointmentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminAppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/admin/appointments")
    public String viewPendingAppointments(HttpSession session, Model model) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        List<Appointment> appointments =
                appointmentRepository.findByStatus(AppointmentStatus.PENDING);

        model.addAttribute("appointments", appointments);
        return "admin-appointments";
    }

    @PostMapping("/admin/appointments/confirm")
    public String confirmAppointment(@RequestParam Long appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId).orElseThrow();

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);

        return "redirect:/admin/appointments";
    }

    @PostMapping("/admin/appointments/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId).orElseThrow();

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        return "redirect:/admin/appointments";
    }
}
