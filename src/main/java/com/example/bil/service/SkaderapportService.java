package com.example.bil.service;

import com.example.bil.model.Skaderapport;
import com.example.bil.repository.SkaderapportRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkaderapportService {

    private final SkaderapportRepo repository;

    public SkaderapportService(SkaderapportRepo repository) {
        this.repository = repository;
    }

    public void opretSkaderapport(Skaderapport rapport) {
        repository.opretSkaderapport(rapport);
    }

    public void updateSkaderapport(Skaderapport rapport) {
        repository.updateSkaderapport(rapport);
    }

    public void sletSkaderapport(int id) {
        repository.sletEfterId(id);
    }

    public List<Skaderapport> getAllSkaderapporter() {
        return repository.fetchAll();
    }

    public Skaderapport findById(int id) {
        return repository.findById(id);
    }

}

