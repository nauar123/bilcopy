package com.example.bil.controller;

import com.example.bil.model.Forretningsudvikler;
import com.example.bil.service.ForretningsudviklerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ForretningsudviklerController {

    //       ---- FORRETNINGSUDVIKLER ----
    @Autowired
    protected ForretningsudviklerService forretningsudviklerService;

    @GetMapping("/forretningsudvikler")
    public String visForretningsudviklerSide(Model model)
    {
        List<Forretningsudvikler> biler = forretningsudviklerService.findUdlejedeBiler();
        double samletIndtaegt = forretningsudviklerService.beregnSamletIndtaegt();
        List<Forretningsudvikler> ledigeBiler = forretningsudviklerService.findAlleLedigeBiler();

        model.addAttribute("ledigeBiler", ledigeBiler);
        model.addAttribute("biler", biler);
        model.addAttribute("samletIndtaegt", samletIndtaegt);

        return "forretningsudvikler";
    }

}
