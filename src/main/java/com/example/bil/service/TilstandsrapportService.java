package com.example.bil.service;

import com.example.bil.model.Tilstandsrapport;
import com.example.bil.repository.TilstandsrapportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TilstandsrapportService {

    @Autowired
    private TilstandsrapportRepo tilstandsrapportRepository;

    // service metode til at hente tilstandsrapporterne
    public List<Tilstandsrapport> getAllTilstandsrapporter() {
        return tilstandsrapportRepository.fetchAll();
    }

    // find tilstandsrapport via id
    public Tilstandsrapport getTilstandsrapportById(int id) {
        return tilstandsrapportRepository.findById(id);
    }

    public int createTilstandsrapport(Tilstandsrapport tilstandsrapport) {
        int result = tilstandsrapportRepository.saveTilstandsrapport(tilstandsrapport);
        System.out.println("Antal rækker indsat: " + result);
        return result;
    }


    //updatere
    public int updateTilstandsrapport(Tilstandsrapport tilstandsrapport) {
        return tilstandsrapportRepository.updateTilstandsrapport(tilstandsrapport);
    }

    //slette
    public int deleteTilstandsrapport(int id) {
        return tilstandsrapportRepository.deleteById(id);
    }
}
