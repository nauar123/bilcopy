package com.example.bil.controller;

import com.example.bil.model.Bil;
import com.example.bil.service.BilService;
import com.example.bil.service.LejekontraktService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ForretningsudviklerController {

    @Autowired
    protected BilService bilService;

    @Autowired
    protected LejekontraktService lejekontraktService;  // TILFØJET: For at beregne rigtig indtægt

    @GetMapping("/biloverblik")
    public String visBiloverblik(HttpSession session, Model model) {
        return visForretningsudviklerSide(session, model);
    }

    @GetMapping("/forretningsudvikler")
    public String visForretningsudviklerSide(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        // Start med tomme lister som fallback
        List<Bil> udlejedeBiler = new ArrayList<>();
        List<Bil> ledigeBiler = new ArrayList<>();
        double samletIndtaegt = 0.0;

        try {
            System.out.println("*** STARTER BIL DATA HENTNING ***");

            // Hent alle biler fra databasen
            List<Bil> alleBiler = bilService.getAllBiler();
            System.out.println("✓ Alle biler hentet: " + alleBiler.size());

            // Filtrer biler efter status
            udlejedeBiler = alleBiler.stream()
                    .filter(bil -> bil.getStatus() == Bil.Status.udlejet)
                    .collect(Collectors.toList());

            ledigeBiler = alleBiler.stream()
                    .filter(bil -> bil.getStatus() == Bil.Status.ledig)
                    .collect(Collectors.toList());

            System.out.println("✓ Udlejede biler: " + udlejedeBiler.size());
            System.out.println("✓ Ledige biler: " + ledigeBiler.size());

            // OPDATERET: Beregn samlet indtægt fra aktive lejekontrakter
            try {
                samletIndtaegt = lejekontraktService.fetchAll().stream()
                        .mapToDouble(kontrakt -> kontrakt.getPris())
                        .sum();
                System.out.println("✓ Samlet indtægt fra lejekontrakter: " + samletIndtaegt);
            } catch (Exception e) {
                System.out.println("⚠️ Kunne ikke beregne indtægt fra lejekontrakter: " + e.getMessage());
                // Fallback til regAfgift
                samletIndtaegt = udlejedeBiler.stream()
                        .mapToDouble(bil -> bil.getRegAfgift())
                        .sum();
            }

        } catch (Exception e) {
            System.out.println("FEJL i bil data hentning: " + e.getMessage());
            e.printStackTrace();
            udlejedeBiler = new ArrayList<>();
            ledigeBiler = new ArrayList<>();
            samletIndtaegt = 0.0;
        }

        // Send data til template
        model.addAttribute("udlejedeBiler", udlejedeBiler);
        model.addAttribute("ledigeBiler", ledigeBiler);
        model.addAttribute("samletIndtaegt", samletIndtaegt);

        System.out.println("*** BIL DATA SENDT TIL VIEW ***");
        return "forretningsudvikler";
    }
}