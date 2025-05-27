package com.example.bil.controller;

import com.example.bil.model.Kunde;
import com.example.bil.service.KundeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class KundeController {

    @Autowired
    private KundeService kundeService;

    // TILFØJET: Alternativ mapping for menubar-linket (lille 'o')
    @GetMapping("/kundeoverblik")
    public String visKunderAlternativ(HttpSession session, Model model) {
        return visKunder(session, model);
    }

    // Eksisterende mapping (stort 'O')
    @GetMapping("/kundeOverblik")
    public String visKunder(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            List<Kunde> kunder = kundeService.fetchAll();
            if (kunder == null) kunder = new ArrayList<>();
            model.addAttribute("kundeListe", kunder);
        } catch (Exception e) {
            System.out.println("Fejl i kundeOverblik: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("kundeListe", new ArrayList<>());
        }

        return "kundeOverblik";
    }

    // Viser kundeoprettelsesformular
    @GetMapping("/opretKunde")
    public String visOpretKunde(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }
        model.addAttribute("kunde", new Kunde());
        return "opretKunde";
    }

    // Modtager kundeoprettelse - TILFØJET detaljeret fejlhåndtering
    @PostMapping("/opretKunde")
    public String opretKunde(@ModelAttribute Kunde kunde, HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== DEBUGGER KUNDE OPRETTELSE ===");
            System.out.println("Kunde ID: " + kunde.getKundeId());
            System.out.println("Navn: " + kunde.getNavn());
            System.out.println("Adresse: " + kunde.getAdresse());
            System.out.println("Telefon: " + kunde.getTelefonnr());
            System.out.println("Email: " + kunde.getEmail());
            System.out.println("Kalder kundeService.opretKunde()...");

            kundeService.opretKunde(kunde);

            System.out.println("Kunde oprettet succesfuldt!");
            return "redirect:/kundeOverblik";
        } catch (Exception e) {
            System.out.println("=== FEJL VED OPRETTELSE AF KUNDE ===");
            System.out.println("Fejltype: " + e.getClass().getSimpleName());
            System.out.println("Fejlbesked: " + e.getMessage());
            System.out.println("Stack trace:");
            e.printStackTrace();

            model.addAttribute("error", "Der opstod en fejl ved oprettelse af kunde: " + e.getMessage());
            model.addAttribute("kunde", kunde);
            return "opretKunde";
        }
    }

    @GetMapping("/updateKunde")
    public String updateKundeOversigt(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }
        model.addAttribute("updateKunde", new Kunde());
        return "updateKunde";
    }

    @GetMapping("/updateKunde/{id}")
    public String visOpdaterKundeFormular(@PathVariable("id") int kundeId, Model model) {
        Kunde kunde = kundeService.findKundeById(kundeId);
        model.addAttribute("updateKunde", kunde);
        return "updateKunde";
    }

    @PostMapping("/updateKunde")
    public String opdaterKunde(@ModelAttribute Kunde kunde) {
        kundeService.opdaterKunde(kunde);
        return "redirect:/kundeOverblik";
    }

    @GetMapping("/slet-kunde/{id}")
    public String sletKunde(@PathVariable("id") int kundeId) {
        kundeService.sletKunde(kundeId);
        return "redirect:/kundeOverblik";
    }
}