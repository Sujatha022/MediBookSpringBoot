package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Patient;
import com.example.demo.model.Role;
import com.example.demo.repository.PatientRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //  LOGIN PAGE 
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // REGISTER PAGE
    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    //  REGISTER LOGIC 
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String phone) {

        if (patientRepository.findByEmail(email) != null) {
            return "redirect:/register?error=email";
        }

        Patient patient = new Patient();
        patient.setName(name);
        patient.setEmail(email);
        patient.setPassword(passwordEncoder.encode(password));
        patient.setPhone(phone);
        patient.setRole(Role.PATIENT);

        patientRepository.save(patient);
        return "redirect:/login?registered";
    }

    
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session) {

        Patient patient = patientRepository.findByEmail(email);

        if (patient != null &&
            passwordEncoder.matches(password, patient.getPassword())) {

            session.setAttribute("patient", patient);
            return "redirect:/dashboard";
        }

        return "redirect:/login?error";
    }

    //DASHBOARD 
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Patient patient = (Patient) session.getAttribute("patient");

        if (patient == null) {
            return "redirect:/login";
        }

        
        model.addAttribute("patientName", patient.getName());

        return "dashboard";
    }

    //  LOGOUT 
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
