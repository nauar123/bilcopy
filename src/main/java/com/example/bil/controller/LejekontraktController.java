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


    //      ---- LEJEKONTRAKT ----
    @Autowired
    private LejekontraktService lejekontraktService;

    // RETTET: lejekontraktOverblik med error handling
    @GetMapping("/lejekontraktOverblik")
    public String visLejekontrakter(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            List<Lejekontrakt> kontrakter = lejekontraktService.fetchAll();
            if (kontrakter == null) kontrakter = new ArrayList<>(); //  kommer null tilbage (hvilket ikke bør ske), laves en tom liste for at undgå fejl i HTML'en.
            model.addAttribute("lejekontraktListe", kontrakter); // Tilføjer listen til Thymeleaf igennem ${lejekontraktListe} i HTML.
        } catch (Exception e) {
            System.out.println("Fejl i lejekontraktOverblik: " + e.getMessage());
            model.addAttribute("lejekontraktListe", new ArrayList<>());
        }

        return "lejekontraktOverblik";
    }

    @GetMapping("/opretLejekontrakt")
    public String visOpretFormular(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        model.addAttribute("lejekontrakt", new Lejekontrakt());
        return "opretLejekontrakt";
    }

    @PostMapping("/opretLejekontrakt")
    public String opretLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt)
    {
        lejekontraktService.addLejekontrakt(lejekontrakt);
        return "redirect:/lejekontraktOverblik";
    }

    // TILFØJET: Generel updateLejekontrakt side (matcher menubar URL)
    @GetMapping("/updateLejekontrakt")
    public String updateLejekontraktOversigt(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        // Vis en tom form eller en liste til at vælge kontrakt at opdatere
        model.addAttribute("updateLejekontrakt", new Lejekontrakt());
        return "updateLejekontrakt";
    }

    @GetMapping("/updateLejekontrakt/{id}")
    public String visOpdaterFormular(@PathVariable("id") int kontraktId, Model model)
    {
        Lejekontrakt lejekontrakt = lejekontraktService.findLejekontraktById(kontraktId);
        model.addAttribute("updateLejekontrakt", lejekontrakt);
        return "updateLejekontrakt";
    }

    @PostMapping("/updateLejekontrakt")
    public String opdaterLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt)
    {
        lejekontraktService.updateLejekontrakt(lejekontrakt);
        return "redirect:/lejekontraktOverblik";
    }

    @GetMapping("/slet-lejekontrakt/{id}")
    public String sletLejekontrakt(@PathVariable("id") int kontraktId)
    {
        lejekontraktService.deleteLejekontrakt(kontraktId);
        return "redirect:/lejekontraktOverblik";
    }

}
