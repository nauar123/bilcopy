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



    // Henter alle lejekontrakter fra databasen.
    public List<Lejekontrakt> fetchAll() {
        return lejekontraktRepo.fetchAll();
    }

    //Tilføjer en ny lejekontrakt til databasen.
    public void addLejekontrakt(Lejekontrakt l)
    {
        lejekontraktRepo.addLejekontrakt(l);
    }

    // Opdaterer en eksisterende lejekontrakt
    public void updateLejekontrakt(Lejekontrakt l) {
        lejekontraktRepo.updateLejekontrakt(l);
    }

    // Finder én lejekontrakt ud fra kontraktId
    public Lejekontrakt findLejekontraktById(int kontraktId) {
        return lejekontraktRepo.findLejekontraktById(kontraktId);
    }

    // Sletter en lejekontrakt ud fra kontraktId
    public void deleteLejekontrakt(int kontraktId) {
        lejekontraktRepo.deleteLejekontrakt(kontraktId);
    }

    // Søger lejekontrakter baseret på søgeord
    public List<Lejekontrakt> searchLejekontrakter(String soegeord) {
        return lejekontraktRepo.searchLejekontrakter(soegeord);
    }

}
