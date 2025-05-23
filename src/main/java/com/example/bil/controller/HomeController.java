package com.example.bil.controller;
import com.example.bil.model.*;
import com.example.bil.model.LoginForm;
import com.example.bil.service.*;
import com.example.bil.service.MedarbejderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class HomeController
{

    //       ---- LOGIN ----
    @Autowired
    protected MedarbejderService medarbejderService;

    @GetMapping("/login")
    public String showLoginForm(Model model)
    {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("adgangskode") String adgangskode,
                               HttpSession session,
                               Model model)
    {

        boolean isValid = medarbejderService.validateLogin(email, adgangskode);

        if (isValid)
        {
            // Hent medarbejderdata og gem i session
            Medarbejder medarbejder = medarbejderService.findByEmail(email);

            session.setAttribute("medarbejderId", medarbejder.getMedarbejderId());
            session.setAttribute("medarbejderNavn", medarbejder.getNavn());
            session.setAttribute("medarbejderRolle", medarbejder.getRolle());
            session.setAttribute("loggedIn", true);

            System.out.println("*** LOGIN GODKENDT FOR: " + medarbejder.getNavn() + " ***");

            // Omdiriger baseret på rolle
            return "redirect:/dashboard";
        } else
        {
            // Vis fejlmeddelelse
            model.addAttribute("error", "Forkert email eller adgangskode");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/login";
    }
    // Redirect fra / til /login
    @GetMapping("/")
    public String home(HttpSession session)
    {
        if (session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
    // Dashboard side
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        model.addAttribute("medarbejderNavn", session.getAttribute("medarbejderNavn"));
        model.addAttribute("medarbejderRolle", session.getAttribute("medarbejderRolle"));

        return "dashboard";
    }


    //       ---- BIL ----
    @Autowired
    protected BilService bilService;

    // Hent alle biler
    @GetMapping("/biler")
    @ResponseBody
    public List<Bil> getAllBiler()
    {
        return bilService.getAllBiler();
    }

    // Hent bil baseret på bil Id
    @GetMapping("/biler/{id}")
    @ResponseBody
    public Bil getBilById(@PathVariable("id") int bilId)
    {
        return bilService.getBilById(bilId);
    }

    // Add bil
    @PostMapping("/biler")
    @ResponseBody
    public void addBil(@RequestBody Bil bil)
    {
        bilService.addBil(bil);
    }

    // Opdatér bil
    @PutMapping("/biler/{id}")
    @ResponseBody
    public void updateBil(@PathVariable("id") int bilId, @RequestBody Bil bil)
    {
        bil.setBilId(bilId);
        bilService.updateBil(bil);
    }

    // Slet bil
    @DeleteMapping("/biler/{id}")
    @ResponseBody
    public void deleteBil(@PathVariable("id") int bilId)
    {
        bilService.deleteBil(bilId);
    }

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

    //      ---- LEJEKONTRAKT ----
    @Autowired
    protected LejekontraktService lejekontraktService;

    @GetMapping("/lejekontraktOverblik")
    public String visLejekontrakter(Model model)
    {
        model.addAttribute("lejekontraktListe", lejekontraktService.fetchAll());
        return "lejekontraktOverblik";
    }

    @GetMapping("/opretLejekontrakt")
    public String visOpretFormular(Model model)
    {
        model.addAttribute("lejekontrakt", new Lejekontrakt());
        return "opretLejekontrakt";
    }

    @PostMapping("/opretLejekontrakt")
    public String opretLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt)
    {
        lejekontraktService.addLejekontrakt(lejekontrakt);
        return "redirect:/lejekontrakter";
    }

    @GetMapping("/updateLejekontrakt/{id}")
    public String visOpdaterFormular(@PathVariable("id") int kontraktId, Model model)
    {
        Lejekontrakt lejekontrakt = lejekontraktService.findLejekontraktById(kontraktId);
        model.addAttribute("updateLejekontrakt", lejekontrakt);
        return "updateLejekontrakt";
    }

    @PostMapping("/updateLejekontrakt")
    public String opdaterLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt)
    {
        lejekontraktService.updateLejekontrakt(lejekontrakt);
        return "redirect:/lejekontrakter";
    }

    @GetMapping("/slet-lejekontrakt/{id}")
    public String sletLejekontrakt(@PathVariable("id") int kontraktId)
    {
        lejekontraktService.deleteLejekontrakt(kontraktId);
        return "redirect:/lejekontrakter";
    }

    //      ---- SKADERAPPORT ----
    @Autowired
    protected SkaderapportService skaderapportService;

    // Oversigt over skaderapporter
    @GetMapping("/skaderapport/liste")
    public String visAlleRapporter(Model model)
    {
        model.addAttribute("skaderapporter", skaderapportService.getAllSkaderapporter());
        return "skaderapport";
    }

    // Opret skaderapport
    @GetMapping("/skaderapport/opret")
    public String visOpretSkadeForm(Model model)
    {
        model.addAttribute("skaderapport", new Skaderapport(0, 0, 0, 0, 0, 0, ""));
        return "opretSkaderapport";
    }

    // Gem skaderapport
    @PostMapping("/skaderapport/gem")
    public String opretSkaderapport(@ModelAttribute Skaderapport skaderapport)
    {
        skaderapportService.opretSkaderapport(skaderapport);
        return "redirect:skaderapport";
    }

    // Slet skaderapport
    @PostMapping("/skaderapport/slet")
    public String sletSkaderapport(@RequestParam("id") int id)
    {
        skaderapportService.sletSkaderapport(id);
        return "redirect:skaderapport";
    }

    // Opdater skaderapport id
    @GetMapping("/skaderapport/opdater/{id}")
    public String visOpdaterForm(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("rapport", skaderapportService.findById(id));
        return "opdaterSkaderapport";
    }

    // Gem opdatering
    @PostMapping("/skaderapport/opdater")
    public String opdaterSkaderapport(@ModelAttribute Skaderapport skaderapport)
    {
        skaderapportService.updateSkaderapport(skaderapport);
        return "redirect:skaderapport";
    }

    //      ---- TILSTANDSRAPPORT ----

    @Autowired
    protected TilstandsrapportService tilstandservice;

    // Gem tilstandsrapport
    @PostMapping("/tilstandsrapport/gem")
    public String gemTilstandsrapport(@ModelAttribute("rapport") Tilstandsrapport rapport)
    {
        tilstandservice.createTilstandsrapport(rapport);
        return "redirect:/tilstandsrapport";
    }

    // Vis liste over alle tilstandsrapporter
    @GetMapping("/tilstandsrapport")
    public String listTilstandsrapporter(Model model)
    {
        List<Tilstandsrapport> rapporter = tilstandservice.getAllTilstandsrapporter();
        model.addAttribute("rapporter", rapporter);
        return "tilstandsrapport";
    }

    // Vis form til at oprette en ny tilstandsrapport
    @GetMapping("/tilstandsrapport/opret")
    public String visOpretTilstandsForm(Model model)
    {
        model.addAttribute("rapport", new Tilstandsrapport());
        return "opretTilstandsrapport";
    }

    // Slet tilstandsrapport via id
    @PostMapping("/tilstandsrapport/slet")
    public String sletTilstandsrapport(@RequestParam("id") int id)
    {
        tilstandservice.deleteTilstandsrapport(id);
        return "redirect:/tilstandsrapport";
    }


}
