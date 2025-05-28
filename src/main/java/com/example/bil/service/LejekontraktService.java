package com.example.bil.service;

import com.example.bil.model.Lejekontrakt;
import com.example.bil.repository.LejekontraktRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LejekontraktService {

    @Autowired
    LejekontraktRepo lejekontraktRepo;

    @Autowired
    JdbcTemplate jdbcTemplate;  // TILFØJET: Direkte database opdatering

    public List<Lejekontrakt> fetchAll() {
        return lejekontraktRepo.fetchAll();
    }

    // OPDATERET: Automatisk bil status opdatering
    public void addLejekontrakt(Lejekontrakt l) {
        try {
            System.out.println("=== OPRETTER LEJEKONTRAKT ===");
            System.out.println("Bil ID: " + l.getBilId());
            System.out.println("Kunde ID: " + l.getKundeId());

            // 1. Opret lejekontrakten
            lejekontraktRepo.addLejekontrakt(l);
            System.out.println("✓ Lejekontrakt oprettet");

            // 2. Opdater bil status DIREKTE i database
            if (l.getBilId() > 0) {
                String sql = "UPDATE bil SET status = 'udlejet' WHERE bil_id = ?";
                int rowsAffected = jdbcTemplate.update(sql, l.getBilId());
                System.out.println("✓ Bil " + l.getBilId() + " sat til UDLEJET (rækker påvirket: " + rowsAffected + ")");
            }

        } catch (Exception e) {
            System.out.println("❌ FEJL ved lejekontrakt oprettelse: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void updateLejekontrakt(Lejekontrakt l) {
        lejekontraktRepo.updateLejekontrakt(l);
    }

    public Lejekontrakt findLejekontraktById(int kontraktId) {
        return lejekontraktRepo.findLejekontraktById(kontraktId);
    }

    // OPDATERET: Sæt bil tilbage til ledig når kontrakt slettes
    public void deleteLejekontrakt(int kontraktId) {
        try {
            System.out.println("=== SLETTER LEJEKONTRAKT " + kontraktId + " ===");

            // 1. Hent bil ID først
            Lejekontrakt lejekontrakt = lejekontraktRepo.findLejekontraktById(kontraktId);
            int bilId = lejekontrakt.getBilId();

            // 2. Slet kontrakten
            lejekontraktRepo.deleteLejekontrakt(kontraktId);
            System.out.println("✓ Lejekontrakt slettet");

            // 3. Sæt bil tilbage til ledig
            if (bilId > 0) {
                String sql = "UPDATE bil SET status = 'ledig' WHERE bil_id = ?";
                int rowsAffected = jdbcTemplate.update(sql, bilId);
                System.out.println("✓ Bil " + bilId + " sat tilbage til LEDIG (rækker påvirket: " + rowsAffected + ")");
            }

        } catch (Exception e) {
            System.out.println("❌ FEJL ved sletning af lejekontrakt: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Lejekontrakt> searchLejekontrakter(String soegeord) {
        return lejekontraktRepo.searchLejekontrakter(soegeord);
    }
}