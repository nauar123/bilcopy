package com.example.bil.controller;

import com.example.bil.model.Kunde;
import com.example.bil.service.KundeService;
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
public class KundeController {

//            -----KUNDE-----

    @Autowired
    private KundeService kundeService;
    // TILFØJET: Kundeoverblik side (matcher menubar URL)

    @GetMapping("/kundeOverblik")
    public String visKunder(HttpSession session, Model model) //Http et midlertidigt hukommelsesrum for én bestemt bruger varer indtil brugeren logger ud eller lukker browseren.
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            List<Kunde> kunder = kundeService.fetchAll();
            if (kunder == null) kunder = new ArrayList<>();
            model.addAttribute("kundeListe", kunder);
        } catch (Exception e) {
            System.out.println("Fejl i kundeOverblik: " + e.getMessage());
            model.addAttribute("kundeListe", new ArrayList<>());
        }

        return "kundeOverblik";
    }


    // Viser kundeoprettelsesformular
    @GetMapping("/opretKunde")
    public String visOpretKunde(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }
        model.addAttribute("kunde", new Kunde());
        return "opretKunde";
    }

    // Modtager kundeoprettelse
    @PostMapping("/opretKunde")
    public String opretKunde(@ModelAttribute Kunde kunde) {
        kundeService.opretKunde(kunde);
        return "redirect:/kundeOverblik";
    }

    @GetMapping("/updateKunde")
    public String updateKundeOversigt(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }
        model.addAttribute("updateKunde", new Kunde());
        return "updateKunde";
    }

    @GetMapping("/updateKunde/{id}")
    public String visOpdaterKundeFormular(@PathVariable("id") int kundeId, Model model) {
        // @PathVariable binder værdien fra URL'en (fx /updateKunde/5) til metoden parameter kundeId
        Kunde kunde = kundeService.findKundeById(kundeId);
        model.addAttribute("updateKunde", kunde);
        return "updateKunde";
    }

    @PostMapping("/updateKunde")
    public String opdaterKunde(@ModelAttribute Kunde kunde) {
        // @ModelAttribute binder formularens data til et Kunde-objekt
        kundeService.opdaterKunde(kunde);
        return "redirect:/kundeOverblik";
    }

    @GetMapping("/slet-kunde/{id}")
    public String sletKunde(@PathVariable("id") int kundeId) {
        kundeService.sletKunde(kundeId);
        return "redirect:/kundeOverblik";
    }
}
