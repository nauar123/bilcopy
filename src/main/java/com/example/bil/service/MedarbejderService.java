package com.example.bil.service;

import com.example.bil.model.Medarbejder;
import com.example.bil.repository.MedarbejderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedarbejderService {

    @Autowired
    private MedarbejderRepo medarbejderRepo;

    public Medarbejder findByEmail(String email) {
        return medarbejderRepo.findByEmail(email);
    }

    public boolean validateLogin(String email, String adgangskode) {
        System.out.println("validateLogin i service: " + email + ", " + adgangskode);

        // Nødløsning - hardcoded login (kommentér ud når det normale login virker)
        if (email.equals("anders@bilabonnement.dk") && adgangskode.equals("test123")) {
            System.out.println("Hardcoded login aktiveret - login godkendt");
            return true;
        }

        // Normal validering
        boolean result = medarbejderRepo.validateLogin(email, adgangskode);
        System.out.println("Database validering resultat: " + result);
        return result;
    }
}