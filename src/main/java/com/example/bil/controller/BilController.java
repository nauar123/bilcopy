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

    // SIMPEL TEST VERSION - uden database kald
    @GetMapping("/biloverblik")
    public String forretningsudvikler(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        // HARDCODED TEST DATA - ingen database kald
        model.addAttribute("biler", new ArrayList<>());
        model.addAttribute("ledigeBiler", new ArrayList<>());
        model.addAttribute("samletIndtaegt", 0.0);

        System.out.println("*** BILOVERBLIK KALDT - TEST DATA SENDT ***");

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

        bilService.addBil(bil);
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