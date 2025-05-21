package com.example.bil.service;

import com.example.bil.model.Medarbejder;

public interface MedarbejderService {
    boolean validateLogin(String email, String adgangskode);
    Medarbejder findByEmail(String email);
}