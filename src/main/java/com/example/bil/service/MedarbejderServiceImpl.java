package com.example.bil.service;

import com.example.bil.model.Medarbejder;
import com.example.bil.repository.MedarbejderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedarbejderServiceImpl implements MedarbejderService {

    @Autowired
    private MedarbejderRepo medarbejderRepo;

    @Override
    public boolean validateLogin(String email, String adgangskode) {
        return medarbejderRepo.validateLogin(email, adgangskode);
    }

    @Override
    public Medarbejder findByEmail(String email) {
        return medarbejderRepo.findByEmail(email);
    }
}