package com.example.bil.service;

import com.example.bil.model.Bil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BilService {

    @Autowired
    private BilRepo bilRepo;

    public List<Bil> getAllBiler()
    {
        return bilRepo.findAll();
    }

    public Bil getBilById(int bilId)
    {
        return bilRepo.findById(bilId);
    }

    public void addBil(Bil bil)
    {
        bilRepo.addBil(bil);
    }

    public void updateBil(Bil bil)
    {
        bilRepo.updateBil(bil);
    }

    public void deleteBil(int bilId)
    {
        bilRepo.deleteById(bilId);
    }
}
