package com.example.bil.controller;

import com.example.bil.model.Skaderapport;
import com.example.bil.service.SkaderapportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/skaderapport")
public class SkaderapportController {

    private final SkaderapportService skaderapportService;

    public SkaderapportController(SkaderapportService skaderapportService) {
        this.skaderapportService = skaderapportService;
    }

    // Oversigt over skaderapporterne
    @GetMapping("/liste")
    public String visAlleRapporter(Model model) {
        model.addAttribute("skaderapporter", skaderapportService.getAllSkaderapporter());
        return "skaderapport";  // navnet på html filen der viser alle skaderapporterne
    }

    // Vis form til at oprette ny skaderapport
    @GetMapping("/opret")
    public String visOpretForm(Model model) {
        model.addAttribute("skaderapport", new Skaderapport(0, 0, 0, 0, 0, 0, ""));
        return "opretSkaderapport";  // Navnet på html filen der opretter skade rapporter
    }

    // Gem ny skaderapport
    @PostMapping("/gem")
    public String opretSkaderapport(@ModelAttribute Skaderapport skaderapport) {
        skaderapportService.opretSkaderapport(skaderapport);
        return "redirect:skaderapport";
    }

    // Slet skaderapport
    @PostMapping("/slet")
    public String sletSkaderapport(@RequestParam("id") int id) {
        skaderapportService.sletSkaderapport(id);
        return "redirect:skaderapport";
    }

    @GetMapping("/opdater/{id}")
    public String visOpdaterForm(@PathVariable("id") int id, Model model) {
        Skaderapport rapport = skaderapportService.findById(id);
        model.addAttribute("rapport", rapport);
        return "opdaterSkaderapport"; // Navn på opdater HTML-fil
    }

    // Gem opdatering
    @PostMapping("/opdater")
    public String opdaterSkaderapport(@ModelAttribute Skaderapport skaderapport) {
        skaderapportService.updateSkaderapport(skaderapport);
        return "redirect:skaderapport";
    }
}
