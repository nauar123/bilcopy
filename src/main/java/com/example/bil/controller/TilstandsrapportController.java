package com.example.bil.controller;

import com.example.bil.model.Tilstandsrapport;
import com.example.bil.service.TilstandsrapportService;
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
public class TilstandsrapportController {

    //      ---- TILSTANDSRAPPORT ----

    @Autowired
    protected TilstandsrapportService tilstandservice;

    // Gem tilstandsrapport
    @PostMapping("/tilstandsrapport/gem")
    public String gemTilstandsrapport(@ModelAttribute("rapport") Tilstandsrapport rapport)
    {
        tilstandservice.createTilstandsrapport(rapport);
        return "redirect:/tilstandsrapport";
    }

    // Vis liste over alle tilstandsrapporter (matcher menubar URL)
    @GetMapping("/tilstandsrapport")
    public String listTilstandsrapporter(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            List<Tilstandsrapport> rapporter = tilstandservice.getAllTilstandsrapporter();
            if (rapporter == null) rapporter = new ArrayList<>();
            model.addAttribute("rapporter", rapporter);
        } catch (Exception e) {
            System.out.println("Fejl i tilstandsrapport: " + e.getMessage());
            model.addAttribute("rapporter", new ArrayList<>());
        }

        return "tilstandsrapport";
    }

    // RETTET: Matcher menubar URL "/opretTilstandsrapport"
    @GetMapping("/opretTilstandsrapport")
    public String visOpretTilstandsForm(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        model.addAttribute("rapport", new Tilstandsrapport());
        return "opretTilstandsrapport";
    }

    // Slet tilstandsrapport via id
    @GetMapping("/tilstandsrapport/slet/{id}")
    public String sletTilstandsrapport(@PathVariable("id") int id)
    {
        tilstandservice.deleteTilstandsrapport(id);
        return "redirect:/tilstandsrapport";
    }

}


