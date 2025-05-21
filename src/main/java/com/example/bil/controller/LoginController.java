package com.example.bil.controller;

import com.example.bil.model.LoginForm;
import com.example.bil.model.Medarbejder;
import com.example.bil.service.MedarbejderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        System.out.println("Login controller modtog login forsøg via @RequestParam: " + email);

        // Validering gennem service
        boolean isValid = medarbejderService.validateLogin(email, adgangskode);
        System.out.println("Service validering resultat: " + isValid);

        if (isValid) {
            // Hent medarbejderdata og gem i session
            Medarbejder medarbejder = medarbejderService.findByEmail(email);

            session.setAttribute("medarbejderId", medarbejder.getMedarbejderId());
            session.setAttribute("medarbejderNavn", medarbejder.getNavn());
            session.setAttribute("medarbejderRolle", medarbejder.getRolle());
            session.setAttribute("loggedIn", true);

            System.out.println("*** LOGIN GODKENDT FOR: " + medarbejder.getNavn() + " ***");

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