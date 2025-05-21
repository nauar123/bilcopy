package com.example.bil.controller;

import com.example.bil.model.Bil;
import com.example.bil.model.Tilstandsrapport;
import com.example.bil.service.TilstandsrapportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tilstandsrapport")
public class TilstandsrapportController {

    @Autowired
    private TilstandsrapportService tilstandservice;

    @PostMapping("/gem")
    public String gemTilstandsrapport(@ModelAttribute("rapport") Tilstandsrapport rapport) {
        tilstandservice.createTilstandsrapport(rapport);
        return "redirect:/tilstandsrapport"; // returnere til siden med listen over tilstandsrapporter
    }


    // Vis liste over alle tilstandsrapporter
    @GetMapping
    public String listTilstandsrapporter(Model model) {
        List<Tilstandsrapport> rapporter = tilstandservice.getAllTilstandsrapporter();
        model.addAttribute("rapporter", rapporter);
        return "tilstandsrapport";  //
    }

    // Vis form til at oprette en ny tilstandsrapport
    @GetMapping("/opret")
    public String visOpretForm(Model model) {
        model.addAttribute("rapport", new Tilstandsrapport());
        return "opretTilstandsrapport";  // navn på html filen med formularen til tilstandsrapporter
    }


    // Slet tilstandsrapport via id
    @PostMapping("/slet")
    public String sletTilstandsrapport(@RequestParam("id") int id) {
        tilstandservice.deleteTilstandsrapport(id);
        return "redirect:/tilstandsrapport";
    }

    // Vis form til opdatering af tilstandsrapport
    @GetMapping("/opdater/{id}")
    public String visOpdaterForm(@PathVariable("id") int id, Model model) {
        Tilstandsrapport rapport = tilstandservice.getTilstandsrapportById(id);
        model.addAttribute("rapport", rapport);
        return "opdaterTilstandsrapport"; // Navn på opdater HTML-fil
    }

    // Opdater tilstandsrapport
    @PostMapping("/opdater")
    public String opdaterTilstandsrapport(@ModelAttribute("rapport") Tilstandsrapport rapport) {
        tilstandservice.updateTilstandsrapport(rapport);
        return "redirect:/tilstandsrapport";
    }
}
