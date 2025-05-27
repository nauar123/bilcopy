package com.example.bil.controller;

import com.example.bil.model.Skaderapport;
import com.example.bil.service.SkaderapportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class skaderapportController {

    @Autowired
    protected SkaderapportService skaderapportService;

    // Vis alle skaderapporter
    @GetMapping("/skaderapport")
    public String visAlleRapporter(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== HENTER ALLE SKADERAPPORTER ===");
            List<Skaderapport> rapporter = skaderapportService.getAllSkaderapporter();
            if (rapporter == null) rapporter = new ArrayList<>();
            System.out.println("Antal rapporter fundet: " + rapporter.size());
            model.addAttribute("rapporter", rapporter);
        } catch (Exception e) {
            System.out.println("FEJL i skaderapport: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("rapporter", new ArrayList<>());
        }

        return "skaderapport";
    }

    // Vis opret skade form
    @GetMapping("/opretskaderapport")
    public String visOpretSkadeForm(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        System.out.println("=== VISER OPRET SKADERAPPORT FORM ===");
        model.addAttribute("skaderapport", new Skaderapport());
        return "opretSkaderapport";
    }

    // Gem skaderapport - RETTET POST mapping
    @PostMapping("/skaderapport/gem")
    public String opretSkaderapport(@ModelAttribute Skaderapport skaderapport, HttpSession session) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== GEMMER SKADERAPPORT ===");
            System.out.println("Modtaget skaderapport: " + skaderapport.getBeskrivelse());
            skaderapportService.opretSkaderapport(skaderapport);
            System.out.println("Skaderapport gemt succesfuldt!");
        } catch (Exception e) {
            System.out.println("FEJL ved gemning af skaderapport: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/skaderapport";
    }

    // Slet skaderapport - RETTET URL mapping
    @PostMapping("/skaderapport/slet")
    public String sletSkaderapport(@RequestParam("id") int id, HttpSession session) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== SLETTER SKADERAPPORT MED ID: " + id + " ===");
            skaderapportService.sletSkaderapport(id);
            System.out.println("Skaderapport slettet succesfuldt!");
        } catch (Exception e) {
            System.out.println("FEJL ved sletning af skaderapport: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/skaderapport";
    }

    // Opdater skaderapport id
    @GetMapping("/skaderapport/opdater/{id}")
    public String visOpdaterForm(@PathVariable("id") int id, Model model, HttpSession session) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            model.addAttribute("rapport", skaderapportService.findById(id));
        } catch (Exception e) {
            System.out.println("FEJL ved hentning af skaderapport til opdatering: " + e.getMessage());
            return "redirect:/skaderapport";
        }
        return "opdaterSkaderapport";
    }

    // Gem opdatering
    @PostMapping("/skaderapport/opdater")
    public String opdaterSkaderapport(@ModelAttribute Skaderapport skaderapport, HttpSession session) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            skaderapportService.updateSkaderapport(skaderapport);
            System.out.println("Skaderapport opdateret succesfuldt!");
        } catch (Exception e) {
            System.out.println("FEJL ved opdatering af skaderapport: " + e.getMessage());
        }

        return "redirect:/skaderapport";
    }
}