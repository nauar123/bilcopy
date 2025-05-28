package com.example.bil.service;

import com.example.bil.model.Bil;
import com.example.bil.repository.BilRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BilService {

    @Autowired
    private BilRepo bilRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate; // TILFØJET: For direkte database queries

    public List<Bil> getAllBiler() {
        return bilRepo.findAll();
    }

    // TILFØJET: Alias metode for at matche LejekontraktController
    public List<Bil> fetchAll() {
        return getAllBiler();
    }

    public Bil getBilById(int bilId) {
        return bilRepo.findById(bilId);
    }

    public void addBil(Bil bil) {
        bilRepo.addBil(bil);
    }

    public void updateBil(Bil bil) {
        bilRepo.updateBil(bil);
    }

    public void deleteBil(int bilId) {
        bilRepo.deleteById(bilId);
    }

    // EKSISTERENDE METODER (bibeholdt)
    public void setBilUdlejet(int bilId) {
        try {
            if (bilRepo.bilExists(bilId)) {
                bilRepo.updateBilStatus(bilId, Bil.Status.udlejet);
                System.out.println("✓ Bil " + bilId + " sat til UDLEJET");
            } else {
                System.out.println("⚠️ Bil " + bilId + " findes ikke i databasen");
            }
        } catch (Exception e) {
            System.out.println("❌ FEJL ved opdatering af bil " + bilId + " til udlejet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setBilLedig(int bilId) {
        try {
            if (bilRepo.bilExists(bilId)) {
                bilRepo.updateBilStatus(bilId, Bil.Status.ledig);
                System.out.println("✓ Bil " + bilId + " sat til LEDIG");
            } else {
                System.out.println("⚠️ Bil " + bilId + " findes ikke i databasen");
            }
        } catch (Exception e) {
            System.out.println("❌ FEJL ved opdatering af bil " + bilId + " til ledig: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Bil.Status getBilStatus(int bilId) {
        try {
            Bil bil = bilRepo.findById(bilId);
            return bil.getStatus();
        } catch (Exception e) {
            System.out.println("❌ FEJL ved hentning af bil status for " + bilId + ": " + e.getMessage());
            return null;
        }
    }

    // ========== NYE METODER TIL SESSION OPDATERING ==========

    /**
     * Hent kun udlejede biler
     */
    public List<Bil> getUdlejdeBiler() {
        try {
            List<Bil> alleBiler = getAllBiler();
            return alleBiler.stream()
                    .filter(bil -> bil.getStatus() == Bil.Status.udlejet)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("❌ FEJL ved hentning af udlejede biler: " + e.getMessage());
            return List.of(); // Tom liste ved fejl
        }
    }

    /**
     * Hent kun ledige biler
     */
    public List<Bil> getLedigeBiler() {
        try {
            List<Bil> alleBiler = getAllBiler();
            return alleBiler.stream()
                    .filter(bil -> bil.getStatus() == Bil.Status.ledig)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("❌ FEJL ved hentning af ledige biler: " + e.getMessage());
            return List.of(); // Tom liste ved fejl
        }
    }

    /**
     * Beregn samlet indtægt fra aktive lejekontrakter
     */
    public double beregnSamletIndtaegt() {
        try {
            String sql = "SELECT COALESCE(SUM(pris), 0.0) FROM lejekontrakt";
            Double result = jdbcTemplate.queryForObject(sql, Double.class);
            return result != null ? result : 0.0;
        } catch (Exception e) {
            System.out.println("❌ FEJL ved beregning af samlet indtægt: " + e.getMessage());
            return 0.0; // Returner 0 ved fejl
        }
    }

    /**
     * Debug metode - print bil statistikker
     */
    public void printBilStatistikker() {
        try {
            List<Bil> alle = getAllBiler();
            List<Bil> udlejede = getUdlejdeBiler();
            List<Bil> ledige = getLedigeBiler();
            double indtaegt = beregnSamletIndtaegt();

            System.out.println("=== BIL STATISTIKKER ===");
            System.out.println("Alle biler: " + alle.size());
            System.out.println("Udlejede: " + udlejede.size());
            System.out.println("Ledige: " + ledige.size());
            System.out.println("Samlet indtægt: " + indtaegt + " kr");
            System.out.println("========================");
        } catch (Exception e) {
            System.out.println("❌ FEJL ved print af bil statistikker: " + e.getMessage());
        }
    }
}