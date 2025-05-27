package com.example.bil.service;

import com.example.bil.model.Kunde;
import com.example.bil.repository.KundeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    public class KundeService {

        @Autowired
        private KundeRepo kundeRepo;

        // Henter alle kunder
        public List<Kunde> fetchAll() {
            return kundeRepo.fetchAll();
        }

        // Finder kunde efter ID
        public Kunde findKundeById(int kundeId) {
            return kundeRepo.findById(kundeId);
        }

        // Opretter ny kunde
        public void opretKunde(Kunde kunde) {
            kundeRepo.addKunde(kunde);
        }


        // Opdaterer eksisterende kunde
        public void opdaterKunde(Kunde kunde) {
            kundeRepo.updateKunde(kunde);
        }

        // Sletter kunde
        public void sletKunde(int kundeId) {
            kundeRepo.deleteById(kundeId);
        }
    }


