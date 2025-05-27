package com.example.bil.controller;

import com.example.bil.model.Lejekontrakt;
import com.example.bil.service.LejekontraktService;
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
public class LejekontraktController {

    @Autowired
    private LejekontraktService lejekontraktService;

    @GetMapping("/lejekontraktOverblik")
    public String visLejekontrakter(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            List<Lejekontrakt> kontrakter = lejekontraktService.fetchAll();
            if (kontrakter == null) kontrakter = new ArrayList<>();
            model.addAttribute("lejekontraktListe", kontrakter);
        } catch (Exception e) {
            System.out.println("Fejl i lejekontraktOverblik: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("lejekontraktListe", new ArrayList<>());
        }

        return "lejekontraktOverblik";
    }

    @GetMapping("/opretLejekontrakt")
    public String visOpretFormular(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("lejekontrakt", new Lejekontrakt());
        return "opretLejekontrakt";
    }

    // TILFØJET: Fejlhåndtering
    @PostMapping("/opretLejekontrakt")
    public String opretLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt, HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== DEBUGGER LEJEKONTRAKT OPRETTELSE ===");
            System.out.println("Kontrakt ID: " + lejekontrakt.getKontraktId());
            System.out.println("Kunde ID: " + lejekontrakt.getKundeId());
            System.out.println("Bil ID: " + lejekontrakt.getBilId());
            System.out.println("Start dato: " + lejekontrakt.getStartDato());
            System.out.println("Slut dato: " + lejekontrakt.getSlutDato());
            System.out.println("Abonnement type: " + lejekontrakt.getAbonnementType());
            System.out.println("Pris: " + lejekontrakt.getPris());
            System.out.println("Medarbejder ID: " + lejekontrakt.getMedarbejderId());

            lejekontraktService.addLejekontrakt(lejekontrakt);
            System.out.println("Lejekontrakt oprettet succesfuldt!");
            return "redirect:/lejekontraktOverblik";
        } catch (Exception e) {
            System.out.println("=== FEJL VED OPRETTELSE AF LEJEKONTRAKT ===");
            System.out.println("Fejltype: " + e.getClass().getSimpleName());
            System.out.println("Fejlbesked: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("error", "Der opstod en fejl ved oprettelse af lejekontrakt: " + e.getMessage());
            model.addAttribute("lejekontrakt", lejekontrakt);
            return "opretLejekontrakt";
        }
    }

    @GetMapping("/updateLejekontrakt")
    public String updateLejekontraktOversigt(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("updateLejekontrakt", new Lejekontrakt());
        return "updateLejekontrakt";
    }

    @GetMapping("/updateLejekontrakt/{id}")
    public String visOpdaterFormular(@PathVariable("id") int kontraktId, Model model) {
        Lejekontrakt lejekontrakt = lejekontraktService.findLejekontraktById(kontraktId);
        model.addAttribute("updateLejekontrakt", lejekontrakt);
        return "updateLejekontrakt";
    }

    @PostMapping("/updateLejekontrakt")
    public String opdaterLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt) {
        lejekontraktService.updateLejekontrakt(lejekontrakt);
        return "redirect:/lejekontraktOverblik";
    }
}