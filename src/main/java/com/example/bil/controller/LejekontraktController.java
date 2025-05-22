package com.example.bil.controller;

import com.example.bil.model.Lejekontrakt;
import com.example.bil.service.LejekontraktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Rigtig import her!
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LejekontraktController {

    @Autowired
    private LejekontraktService lejekontraktService;

    @GetMapping("/lejekontraktOverblik")
    public String visLejekontrakter(Model model) {
        List<Lejekontrakt> lejekontraktListe = lejekontraktService.fetchAll();
        model.addAttribute("lejekontraktListe", lejekontraktListe);
        return "lejekontraktOverblik"; // returner filnavn uden "/"
    }

    @GetMapping("/opretLejekontrakt")
    public String visOpretFormular(Model model) {
        model.addAttribute("lejekontrakt", new Lejekontrakt()); // klasse med stort L
        return "opretLejekontrakt";
    }

    @PostMapping("/opretLejekontrakt")
    public String opretLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt) {
        lejekontraktService.addLejekontrakt(lejekontrakt);
        return "redirect:/lejekontrakter";
    }


    @GetMapping("/updateLejekontrakt/{id}")
    public String visOpdaterFormular(@PathVariable("id") int kontraktId, Model model) {
        Lejekontrakt lejekontrakt = lejekontraktService.findLejekontraktById(kontraktId);
        model.addAttribute("updateLejekontrakt", lejekontrakt);
        return "updateLejekontrakt";
    }

    @PostMapping("/updateLejekontrakt")
    public String opdaterLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt) {
        lejekontraktService.updateLejekontrakt(lejekontrakt);
        return "redirect:/lejekontrakter";
    }

    @GetMapping("/slet-lejekontrakt/{id}")
    public String sletLejekontrakt(@PathVariable("id") int kontraktId) {
        lejekontraktService.deleteLejekontrakt(kontraktId);
        return "redirect:/lejekontrakter";
    }

    @GetMapping("/soeg-lejekontrakter")
    public String soegLejekontrakter(@RequestParam("soegeord") String soegeord, Model model) {
        List<Lejekontrakt> resultat = lejekontraktService.searchLejekontrakter(soegeord);
        model.addAttribute("lejekontraktListe", resultat);
        return "lejekontraktOverblik";
    }
}
