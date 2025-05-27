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

    //      ---- SKADERAPPORT ----
    @Autowired
    protected SkaderapportService skaderapportService;

    // RETTET: Matcher menubar URL "/skaderapport"
    @GetMapping("/skaderapport")
    public String visAlleRapporter(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            List<Skaderapport> rapporter = skaderapportService.getAllSkaderapporter();
            if (rapporter == null) rapporter = new ArrayList<>();
            model.addAttribute("rapporter", rapporter);
        } catch (Exception e) {
            System.out.println("Fejl i skaderapport: " + e.getMessage());
            model.addAttribute("rapporter", new ArrayList<>());
        }

        return "skaderapport";
    }

    // RETTET: Matcher menubar URL "/opretskaderapport"
    @GetMapping("/opretskaderapport")
    public String visOpretSkadeForm(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        model.addAttribute("skaderapport", new Skaderapport(0, 0, 0, 0, 0, 0, ""));
        return "opretSkaderapport";
    }

    // Gem skaderapport
    @PostMapping("/skaderapport/gem")
    public String opretSkaderapport(@ModelAttribute Skaderapport skaderapport)
    {
        skaderapportService.opretSkaderapport(skaderapport);
        return "redirect:/skaderapport";
    }

    // Slet skaderapport
    @PostMapping("/skaderapport/slet")
    public String sletSkaderapport(@RequestParam("id") int id)
    {
        skaderapportService.sletSkaderapport(id);
        return "redirect:/skaderapport";
    }

    // Opdater skaderapport id
    @GetMapping("/skaderapport/opdater/{id}")
    public String visOpdaterForm(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("rapport", skaderapportService.findById(id));
        return "opdaterSkaderapport";
    }

    // Gem opdatering
    @PostMapping("/skaderapport/opdater")
    public String opdaterSkaderapport(@ModelAttribute Skaderapport skaderapport)
    {
        skaderapportService.updateSkaderapport(skaderapport);
        return "redirect:/skaderapport";
    }

}
