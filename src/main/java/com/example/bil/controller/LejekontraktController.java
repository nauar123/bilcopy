package com.example.bil.controller;

import com.example.bil.model.Lejekontrakt;
import com.example.bil.service.LejekontraktService;
import com.example.bil.service.BilService; // TILFØJ DENNE IMPORT
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // TILFØJ DENNE IMPORT
import com.example.bil.model.Bil; // TILFØJ DENNE LINJE

import java.util.ArrayList;
import java.util.List;

@Controller
public class LejekontraktController {

    @Autowired
    private LejekontraktService lejekontraktService;

    @Autowired
    private BilService bilService; // TILFØJET: For at opdatere bil-data

    @GetMapping("/lejekontraktOverblik")
    public String visLejekontrakter(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== HENTER LEJEKONTRAKTER ===");
            List<Lejekontrakt> kontrakter = lejekontraktService.fetchAll();
            System.out.println("Antal kontrakter fundet: " + (kontrakter != null ? kontrakter.size() : "null"));

            if (kontrakter == null) kontrakter = new ArrayList<>();
            model.addAttribute("lejekontraktListe", kontrakter);

            System.out.println("Lejekontrakter sendt til view");
        } catch (Exception e) {
            System.out.println("Fejl i lejekontraktOverblik: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("lejekontraktListe", new ArrayList<>());
        }

        return "lejekontraktOverblik";
    }

    @GetMapping("/opretLejekontrakt")
    public String visOpretFormular(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("lejekontrakt", new Lejekontrakt());
        return "opretLejekontrakt";
    }

    @PostMapping("/opretLejekontrakt")
    public String opretLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt, HttpSession session,
                                    Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== DEBUGGER LEJEKONTRAKT OPRETTELSE ===");
            System.out.println("Kontrakt ID: " + lejekontrakt.getKontraktId());
            System.out.println("Kunde ID: " + lejekontrakt.getKundeId());
            System.out.println("Bil ID: " + lejekontrakt.getBilId());
            System.out.println("Start dato: " + lejekontrakt.getStartDato());
            System.out.println("Slut dato: " + lejekontrakt.getSlutDato());
            System.out.println("Abonnement type: " + lejekontrakt.getAbonnementType());
            System.out.println("Pris: " + lejekontrakt.getPris());
            System.out.println("Medarbejder ID: " + lejekontrakt.getMedarbejderId());

            lejekontraktService.addLejekontrakt(lejekontrakt);
            System.out.println("Lejekontrakt oprettet succesfuldt!");

            // TILFØJ: Opdater session med friske bil-data
            opdaterBilDataISession(session);

            redirectAttributes.addFlashAttribute("successMessage", "Lejekontrakt oprettet succesfuldt!");
            return "redirect:/lejekontraktOverblik";
        } catch (Exception e) {
            System.out.println("=== FEJL VED OPRETTELSE AF LEJEKONTRAKT ===");
            System.out.println("Fejltype: " + e.getClass().getSimpleName());
            System.out.println("Fejlbesked: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("error", "Der opstod en fejl ved oprettelse af lejekontrakt: " + e.getMessage());
            model.addAttribute("lejekontrakt", lejekontrakt);
            return "opretLejekontrakt";
        }
    }

    @GetMapping("/updateLejekontrakt")
    public String updateLejekontraktOversigt(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("updateLejekontrakt", new Lejekontrakt());
        return "updateLejekontrakt";
    }

    @GetMapping("/updateLejekontrakt/{id}")
    public String visOpdaterFormular(@PathVariable("id") int kontraktId, Model model) {
        Lejekontrakt lejekontrakt = lejekontraktService.findLejekontraktById(kontraktId);
        model.addAttribute("updateLejekontrakt", lejekontrakt);
        return "updateLejekontrakt";
    }

    @PostMapping("/updateLejekontrakt")
    public String opdaterLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt, HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        // TILFØJ: Session check
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        lejekontraktService.updateLejekontrakt(lejekontrakt);

        // TILFØJ: Opdater session med friske bil-data
        opdaterBilDataISession(session);

        redirectAttributes.addFlashAttribute("successMessage", "Lejekontrakt opdateret succesfuldt!");
        return "redirect:/lejekontraktOverblik";
    }

    // GET mapping (original)
    @GetMapping("/slet-lejekontrakt/{id}")
    public String sletLejekontrakt(@PathVariable("id") int kontraktId, HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== GET CONTROLLER: SLETTER LEJEKONTRAKT " + kontraktId + " ===");
            lejekontraktService.deleteLejekontrakt(kontraktId);
            System.out.println("✓ GET Controller: Lejekontrakt " + kontraktId + " slettet succesfuldt");

            // VIGTIG TILFØJELSE: Opdater bil-data i session
            opdaterBilDataISession(session);

            redirectAttributes.addFlashAttribute("successMessage", "Lejekontrakt slettet succesfuldt!");
            return "redirect:/lejekontraktOverblik";
        } catch (Exception e) {
            System.out.println("❌ GET CONTROLLER FEJL ved sletning af lejekontrakt " + kontraktId + ": " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved sletning af lejekontrakt!");
            return "redirect:/lejekontraktOverblik";
        }
    }

    // TILFØJET: POST mapping for slet lejekontrakt
    @PostMapping("/slet-lejekontrakt")
    public String sletLejekontraktPost(@RequestParam("id") int kontraktId, HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        try {
            System.out.println("=== POST CONTROLLER: SLETTER LEJEKONTRAKT " + kontraktId + " ===");
            lejekontraktService.deleteLejekontrakt(kontraktId);
            System.out.println("✓ POST Controller: Lejekontrakt " + kontraktId + " slettet succesfuldt");

            // VIGTIG TILFØJELSE: Opdater bil-data i session
            opdaterBilDataISession(session);

            redirectAttributes.addFlashAttribute("successMessage", "Lejekontrakt slettet succesfuldt!");
            return "redirect:/lejekontraktOverblik";
        } catch (Exception e) {
            System.out.println("❌ POST CONTROLLER FEJL ved sletning af lejekontrakt " + kontraktId + ": " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved sletning af lejekontrakt!");
            return "redirect:/lejekontraktOverblik";
        }
    }

    // NYE HJÆLPE-METODE: Opdater bil-data i session
    private void opdaterBilDataISession(HttpSession session) {
        try {
            System.out.println("=== OPDATERER BIL-DATA I SESSION ===");

            // Hent friske bil-data fra BilService
            List<Bil> alleBiler = bilService.getAllBiler();
            List<Bil> udlejdeBiler = bilService.getUdlejdeBiler();
            List<Bil> ledigeBiler = bilService.getLedigeBiler();
            double samletIndtaegt = bilService.beregnSamletIndtaegt();

            // Opdater session med friske data
            session.setAttribute("alleBiler", alleBiler);
            session.setAttribute("udlejdeBiler", udlejdeBiler);
            session.setAttribute("ledigeBiler", ledigeBiler);
            session.setAttribute("samletIndtaegt", samletIndtaegt);

            // Debug print
            System.out.println("✓ Session opdateret med friske bil-data");
            System.out.println("  - Alle biler: " + alleBiler.size());
            System.out.println("  - Udlejede: " + udlejdeBiler.size());
            System.out.println("  - Ledige: " + ledigeBiler.size());
            System.out.println("  - Samlet indtægt: " + samletIndtaegt + " kr");

        } catch (Exception e) {
            System.out.println("❌ FEJL ved opdatering af bil-data i session: " + e.getMessage());
            e.printStackTrace();
        }
    }
}