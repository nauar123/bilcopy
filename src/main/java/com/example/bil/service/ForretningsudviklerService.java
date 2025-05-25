package com.example.bil.service;
import com.example.bil.model.Forretningsudvikler;
import com.example.bil.repository.ForretningsudviklerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ForretningsudviklerService
{
    @Autowired
    protected ForretningsudviklerRepo forretningsudviklerRepo;

    public List<Forretningsudvikler> findAlle()
    {
        return forretningsudviklerRepo.findAll();
    }

    // find ledige biler
    public List<Forretningsudvikler> findAlleLedigeBiler()
    {
        return forretningsudviklerRepo.findByStatus("ledig");
    }

    // find uledige biler
    public List<Forretningsudvikler> findUdlejedeBiler()
    {
        return forretningsudviklerRepo.findByStatus("ikke ledig");
    }

    public double beregnSamletIndtaegt()
    {
        List<Forretningsudvikler> biler = forretningsudviklerRepo.findAll();
        return biler.stream().mapToDouble(Forretningsudvikler::getSamletIndtaegt).sum();
    }
}