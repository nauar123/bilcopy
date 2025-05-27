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


    //       ---- BIL ----
    @Autowired
    protected BilService bilService;
    protected ForretningsudviklerService forretningsudviklerService;


    // RETTET: Biloverblik side med error handling
    @GetMapping("/biloverblik")
    public String forretningsudvikler(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            // Hent bil data via forretningsudvikler service
            List<Forretningsudvikler> biler = forretningsudviklerService.findUdlejedeBiler();
            List<Forretningsudvikler> ledigeBiler = forretningsudviklerService.findAlleLedigeBiler();
            double samletIndtaegt = forretningsudviklerService.beregnSamletIndtaegt();

            // Sørg for at listerne ikke er null
            if (biler == null) biler = new ArrayList<>();
            if (ledigeBiler == null) ledigeBiler = new ArrayList<>();

            model.addAttribute("biler", biler);
            model.addAttribute("ledigeBiler", ledigeBiler);
            model.addAttribute("samletIndtaegt", samletIndtaegt);

        } catch (Exception e) {
            // Log fejlen og send tomme lister
            System.out.println("Fejl i biloverblik: " + e.getMessage());
            model.addAttribute("biler", new ArrayList<>());
            model.addAttribute("ledigeBiler", new ArrayList<>());
            model.addAttribute("samletIndtaegt", 0.0);
        }

        return "forretningsudvikler";
    }

    // TILFØJET: Manglende bil/opret mapping
    @GetMapping("/bil/opret")
    public String opretBil(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("bil", new Bil());
        return "opretBil"; // Du skal oprette opretBil.html fil
    }

    // TILFØJ: POST mapping for bil/opret
    @PostMapping("/bil/opret")
    public String gemNyBil(@ModelAttribute Bil bil, HttpSession session) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        bilService.addBil(bil);
        return "redirect:/biloverblik";
    }


    // Hent alle biler
    @GetMapping("/biler")
    @ResponseBody
    public List<Bil> getAllBiler() {
        return bilService.getAllBiler();
    }

    // Hent bil baseret på bil Id
    @GetMapping("/biler/{id}")
    @ResponseBody
    public Bil getBilById(@PathVariable("id") int bilId) {
        return bilService.getBilById(bilId);
    }

    // Add bil
    @PostMapping("/biler")
    @ResponseBody
    public void addBil(@RequestBody Bil bil) {
        bilService.addBil(bil);
    }

    // Opdatér bil
    @PutMapping("/biler/{id}")
    @ResponseBody
    public void updateBil(@PathVariable("id") int bilId, @RequestBody Bil bil) {
        bil.setBilId(bilId);
        bilService.updateBil(bil);
    }

    // Slet bil
    @DeleteMapping("/biler/{id}")
    @ResponseBody
    public void deleteBil(@PathVariable("id") int bilId) {
        bilService.deleteBil(bilId);
    }
}
