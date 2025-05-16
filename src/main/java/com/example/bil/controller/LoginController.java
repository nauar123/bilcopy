package com.example.bil.controller;

import com.example.bil.model.LoginForm;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.bil.model.Medarbejder;
import com.example.bil.service.MedarbejderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private MedarbejderService medarbejderService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("adgangskode") String adgangskode,
                               HttpSession session,
                               Model model) {

        System.out.println("Login controller modtog login forsøg via @RequestParam: " + email + ", " + adgangskode);

        // Direkte hardcoded check
        if ("anders@bilabonnement.dk".equals(email) &&
                "test123".equals(adgangskode)) {

            System.out.println("*** HARDCODED LOGIN MATCH I CONTROLLER - LOGIN GODKENDT ***");

            // Sæt session-variable manuelt
            session.setAttribute("medarbejderId", 1);
            session.setAttribute("medarbejderNavn", "Anders Andersen");
            session.setAttribute("medarbejderRolle", "dataregistrering");
            session.setAttribute("loggedIn", true);

            return "redirect:/dashboard";
        }

        // Normal validering gennem service
        boolean isValid = medarbejderService.validateLogin(email, adgangskode);
        System.out.println("Service validering resultat: " + isValid);

        if (isValid) {
            // Hent medarbejderdata og gem i session
            Medarbejder medarbejder = medarbejderService.findByEmail(email);

            session.setAttribute("medarbejderId", medarbejder.getMedarbejderId());
            session.setAttribute("medarbejderNavn", medarbejder.getNavn());
            session.setAttribute("medarbejderRolle", medarbejder.getRolle());
            session.setAttribute("loggedIn", true);

            // Omdiriger baseret på rolle
            return "redirect:/dashboard";
        } else {
            // Vis fejlmeddelelse
            model.addAttribute("error", "Forkert email eller adgangskode");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Redirect fra / til /login
    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn")) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    // Dashboard side
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("medarbejderNavn", session.getAttribute("medarbejderNavn"));
        model.addAttribute("medarbejderRolle", session.getAttribute("medarbejderRolle"));

        return "dashboard";
    }
}