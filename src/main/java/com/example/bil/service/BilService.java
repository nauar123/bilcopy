package com.example.bil.service;

import com.example.bil.model.Bil;
import com.example.bil.repository.BilRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BilService {

    @Autowired
    private BilRepo bilRepo;

    public List<Bil> getAllBiler() {
        return bilRepo.findAll();
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

    // TILFØJET: Sæt bil som udlejet
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

    // TILFØJET: Sæt bil som ledig (når lejekontrakt slettes/afsluttes)
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

    // TILFØJET: Tjek bil status
    public Bil.Status getBilStatus(int bilId) {
        try {
            Bil bil = bilRepo.findById(bilId);
            return bil.getStatus();
        } catch (Exception e) {
            System.out.println("❌ FEJL ved hentning af bil status for " + bilId + ": " + e.getMessage());
            return null;
        }
    }
}