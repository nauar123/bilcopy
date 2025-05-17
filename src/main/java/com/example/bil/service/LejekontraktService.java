package com.example.bil.service;

import com.example.bil.model.Lejekontrakt;
import com.example.bil.repository.LejekontraktRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository


public class LejekontraktService {
    @Autowired
    LejekontraktRepo lejekontraktRepo;




    public List<Lejekontrakt> fetchAll() {
        return lejekontraktRepo.fetchAll();
    }


    public void addLejekontrakt(Lejekontrakt l)
    {
        lejekontraktRepo.addLejekontrakt(l);
    }
}
