package com.example.bil.controller;
import com.example.bil.model.Bil;
import com.example.bil.service.BilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/biler")
public class BilController
{

    @Autowired
    protected BilService bilService;

    // hent alle biler
    @GetMapping
    public List<Bil> getAllBiler()
    {
        return bilService.getAllBiler();
    }

    // hent bil efter bilId
    @GetMapping("/{id}")
    public Bil getBilById(@PathVariable("id") int bilId)
    {
        return bilService.getBilById(bilId);
    }

    // add bil
    @PostMapping
    public void addBil(@RequestBody Bil bil)
    {
        bilService.addBil(bil);
    }

    // opdater bil
    @PutMapping("/{id}")
    public void updateBil(@PathVariable("id") int bilId, @RequestBody Bil bil)
    {
        bil.setBilId(bilId);
        bilService.updateBil(bil);
    }

    // slet bil
    @DeleteMapping("/{id}")
    public void deleteBil(@PathVariable("id") int bilId)
    {
        bilService.deleteBil(bilId);
    }
}
