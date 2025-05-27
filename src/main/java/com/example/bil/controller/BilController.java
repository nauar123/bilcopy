package com.example.bil.controller;

import com.example.bil.model.Bil;
import com.example.bil.model.Forretningsudvikler;
import com.example.bil.service.BilService;
import com.example.bil.service.ForretningsudviklerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BilController {

    @Autowired
    protected BilService bilService;

    @Autowired
    protected ForretningsudviklerService forretningsudviklerService;

    @GetMapping("/biloverblik")
    public String forretningsudvikler(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        // Start med tomme lister som fallback
        List<Forretningsudvikler> biler = new ArrayList<>();
        List<Forretningsudvikler> ledigeBiler = new ArrayList<>();
        double samletIndtaegt = 0.0;

        try {
            System.out.println("*** STARTER BILOVERBLIK DATA HENTNING ***");

            // Test om service er tilgængelig
            if (forretningsudviklerService == null) {
                System.out.println("ERROR: forretningsudviklerService er null!");
                throw new RuntimeException("ForretningsudviklerService er ikke tilgængelig");
            }

            // Hent data en ad gangen med individuel fejlhåndtering
            try {
                biler = forretningsudviklerService.findUdlejedeBiler();
                System.out.println("✓ Udlejede biler hentet: " + (biler != null ? biler.size() : "null"));
            } catch (Exception e) {
                System.out.println("FEJL ved hentning af udlejede biler: " + e.getMessage());
                biler = new ArrayList<>();
            }

            try {
                ledigeBiler = forretningsudviklerService.findAlleLedigeBiler();
                System.out.println("✓ Ledige biler hentet: " + (ledigeBiler != null ? ledigeBiler.size() : "null"));
            } catch (Exception e) {
                System.out.println("FEJL ved hentning af ledige biler: " + e.getMessage());
                ledigeBiler = new ArrayList<>();
            }

            try {
                samletIndtaegt = forretningsudviklerService.beregnSamletIndtaegt();
                System.out.println("✓ Samlet indtægt beregnet: " + samletIndtaegt);
            } catch (Exception e) {
                System.out.println("FEJL ved beregning af indtægt: " + e.getMessage());
                samletIndtaegt = 0.0;
            }

        } catch (Exception e) {
            System.out.println("GENEREL FEJL i biloverblik: " + e.getMessage());
            e.printStackTrace();
        }

        // Sørg for at listerne aldrig er null
        if (biler == null) biler = new ArrayList<>();
        if (ledigeBiler == null) ledigeBiler = new ArrayList<>();

        model.addAttribute("biler", biler);
        model.addAttribute("ledigeBiler", ledigeBiler);
        model.addAttribute("samletIndtaegt", samletIndtaegt);

        System.out.println("*** BILOVERBLIK DATA SENDT TIL VIEW ***");
        return "forretningsudvikler";
    }

    @GetMapping("/bil/opret")
    public String opretBil(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("bil", new Bil());
        return "opretBil";
    }

    @PostMapping("/bil/opret")
    public String gemNyBil(@ModelAttribute Bil bil, HttpSession session) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            bilService.addBil(bil);
        } catch (Exception e) {
            System.out.println("FEJL ved oprettelse af bil: " + e.getMessage());
        }

        return "redirect:/biloverblik";
    }

    // REST endpoints
    @GetMapping("/biler")
    @ResponseBody
    public List<Bil> getAllBiler() {
        return bilService.getAllBiler();
    }

    @GetMapping("/biler/{id}")
    @ResponseBody
    public Bil getBilById(@PathVariable("id") int bilId) {
        return bilService.getBilById(bilId);
    }

    @PostMapping("/biler")
    @ResponseBody
    public void addBil(@RequestBody Bil bil) {
        bilService.addBil(bil);
    }

    @PutMapping("/biler/{id}")
    @ResponseBody
    public void updateBil(@PathVariable("id") int bilId, @RequestBody Bil bil) {
        bil.setBilId(bilId);
        bilService.updateBil(bil);
    }

    @DeleteMapping("/biler/{id}")
    @ResponseBody
    public void deleteBil(@PathVariable("id") int bilId) {
        bilService.deleteBil(bilId);
    }
}