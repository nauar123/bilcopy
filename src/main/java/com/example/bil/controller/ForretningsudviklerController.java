package com.example.bil.controller;

import com.example.bil.model.Forretningsudvikler;
import com.example.bil.service.ForretningsudviklerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ForretningsudviklerController {

    @Autowired
    protected ForretningsudviklerService forretningsudviklerService;

    @GetMapping("/forretningsudvikler")
    public String visForretningsudviklerSide(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        // Start med tomme lister som fallback
        List<Forretningsudvikler> biler = new ArrayList<>();
        List<Forretningsudvikler> ledigeBiler = new ArrayList<>();
        double samletIndtaegt = 0.0;

        try {
            System.out.println("*** STARTER FORRETNINGSUDVIKLER DATA HENTNING ***");

            // Hent data med fejlhåndtering
            try {
                biler = forretningsudviklerService.findUdlejedeBiler();
                System.out.println("✓ Udlejede biler hentet: " + (biler != null ? biler.size() : "null"));
            } catch (Exception e) {
                System.out.println("FEJL ved hentning af udlejede biler: " + e.getMessage());
                e.printStackTrace();
                biler = new ArrayList<>();
            }

            try {
                ledigeBiler = forretningsudviklerService.findAlleLedigeBiler();
                System.out.println("✓ Ledige biler hentet: " + (ledigeBiler != null ? ledigeBiler.size() : "null"));
            } catch (Exception e) {
                System.out.println("FEJL ved hentning af ledige biler: " + e.getMessage());
                e.printStackTrace();
                ledigeBiler = new ArrayList<>();
            }

            try {
                samletIndtaegt = forretningsudviklerService.beregnSamletIndtaegt();
                System.out.println("✓ Samlet indtægt beregnet: " + samletIndtaegt);
            } catch (Exception e) {
                System.out.println("FEJL ved beregning af indtægt: " + e.getMessage());
                e.printStackTrace();
                samletIndtaegt = 0.0;
            }

        } catch (Exception e) {
            System.out.println("GENEREL FEJL i forretningsudvikler: " + e.getMessage());
            e.printStackTrace();
        }

        // Sørg for at listerne aldrig er null
        if (biler == null) biler = new ArrayList<>();
        if (ledigeBiler == null) ledigeBiler = new ArrayList<>();

        model.addAttribute("biler", biler);
        model.addAttribute("ledigeBiler", ledigeBiler);
        model.addAttribute("samletIndtaegt", samletIndtaegt);

        System.out.println("*** FORRETNINGSUDVIKLER DATA SENDT TIL VIEW ***");
        return "forretningsudvikler";
    }
}