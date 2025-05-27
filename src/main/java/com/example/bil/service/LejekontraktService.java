package com.example.bil.service;

import com.example.bil.model.Lejekontrakt;
import com.example.bil.repository.LejekontraktRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LejekontraktService {

    @Autowired
    LejekontraktRepo lejekontraktRepo;

    @Autowired
    BilService bilService;  // TILFØJET: For at opdatere bil status

    // Henter alle lejekontrakter fra databasen.
    public List<Lejekontrakt> fetchAll() {
        return lejekontraktRepo.fetchAll();
    }

    // OPDATERET: Tilføjer en ny lejekontrakt og opdaterer bil status
    public void addLejekontrakt(Lejekontrakt l) {
        try {
            System.out.println("=== OPRETTER LEJEKONTRAKT ===");
            System.out.println("Bil ID: " + l.getBilId());
            System.out.println("Kunde ID: " + l.getKundeId());

            // Opret lejekontrakten
            lejekontraktRepo.addLejekontrakt(l);
            System.out.println("✓ Lejekontrakt oprettet i database");

            // Opdater bil status til udlejet
            bilService.setBilUdlejet(l.getBilId());
            System.out.println("✓ Bil " + l.getBilId() + " markeret som udlejet");

        } catch (Exception e) {
            System.out.println("❌ FEJL ved oprettelse af lejekontrakt: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw så controller kan håndtere fejlen
        }
    }

    // Opdaterer en eksisterende lejekontrakt
    public void updateLejekontrakt(Lejekontrakt l) {
        lejekontraktRepo.updateLejekontrakt(l);
    }

    // Finder én lejekontrakt ud fra kontraktId
    public Lejekontrakt findLejekontraktById(int kontraktId) {
        return lejekontraktRepo.findLejekontraktById(kontraktId);
    }

    // OPDATERET: Sletter en lejekontrakt og sætter bil tilbage til ledig
    public void deleteLejekontrakt(int kontraktId) {
        try {
            System.out.println("=== SLETTER LEJEKONTRAKT " + kontraktId + " ===");

            // Hent lejekontrakten først for at få bil ID
            Lejekontrakt lejekontrakt = lejekontraktRepo.findLejekontraktById(kontraktId);
            int bilId = lejekontrakt.getBilId();

            // Slet lejekontrakten
            lejekontraktRepo.deleteLejekontrakt(kontraktId);
            System.out.println("✓ Lejekontrakt " + kontraktId + " slettet");

            // Sæt bil tilbage til ledig
            bilService.setBilLedig(bilId);
            System.out.println("✓ Bil " + bilId + " sat tilbage til ledig");

        } catch (Exception e) {
            System.out.println("❌ FEJL ved sletning af lejekontrakt: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Søger lejekontrakter baseret på søgeord
    public List<Lejekontrakt> searchLejekontrakter(String soegeord) {
        return lejekontraktRepo.searchLejekontrakter(soegeord);
    }
}