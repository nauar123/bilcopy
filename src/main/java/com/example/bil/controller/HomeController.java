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
import java.util.ArrayList;

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

    // RETTET: Biloverblik side med error handling
    @GetMapping("/biloverblik")
    public String biloverblik(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            // Hent bil data via forretningsudvikler service
            List<Forretningsudvikler> biler = forretningsudviklerService.findUdlejedeBiler();
            List<Forretningsudvikler> ledigeBiler = forretningsudviklerService.findAlleLedigeBiler();
            double samletIndtaegt = forretningsudviklerService.beregnSamletIndtaegt();

            // Sørg for at listerne ikke er null
            if (biler == null) biler = new ArrayList<>();
            if (ledigeBiler == null) ledigeBiler = new ArrayList<>();

            model.addAttribute("biler", biler);
            model.addAttribute("ledigeBiler", ledigeBiler);
            model.addAttribute("samletIndtaegt", samletIndtaegt);

        } catch (Exception e) {
            // Log fejlen og send tomme lister
            System.out.println("Fejl i biloverblik: " + e.getMessage());
            model.addAttribute("biler", new ArrayList<>());
            model.addAttribute("ledigeBiler", new ArrayList<>());
            model.addAttribute("samletIndtaegt", 0.0);
        }

        return "biloverblik";
    }

    // TILFØJET: Manglende bil/opret mapping
    @GetMapping("/bil/opret")
    public String opretBil(HttpSession session, Model model) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        model.addAttribute("bil", new Bil());
        return "opretBil"; // Du skal oprette opretBil.html fil
    }

    // TILFØJ: POST mapping for bil/opret
    @PostMapping("/bil/opret")
    public String gemNyBil(@ModelAttribute Bil bil, HttpSession session) {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn")) {
            return "redirect:/login";
        }

        bilService.addBil(bil);
        return "redirect:/biloverblik";
    }

    // TILFØJET: Kundeoverblik side (matcher menubar URL)
    @GetMapping("/kundeoverblik")
    public String kundeoverblik(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        return "kundeoverblik";
    }

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

    // RETTET: lejekontraktOverblik med error handling
    @GetMapping("/lejekontraktOverblik")
    public String visLejekontrakter(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            List<Lejekontrakt> kontrakter = lejekontraktService.fetchAll();
            if (kontrakter == null) kontrakter = new ArrayList<>();
            model.addAttribute("lejekontraktListe", kontrakter);
        } catch (Exception e) {
            System.out.println("Fejl i lejekontraktOverblik: " + e.getMessage());
            model.addAttribute("lejekontraktListe", new ArrayList<>());
        }

        return "lejekontraktOverblik";
    }

    @GetMapping("/opretLejekontrakt")
    public String visOpretFormular(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        model.addAttribute("lejekontrakt", new Lejekontrakt());
        return "opretLejekontrakt";
    }

    @PostMapping("/opretLejekontrakt")
    public String opretLejekontrakt(@ModelAttribute Lejekontrakt lejekontrakt)
    {
        lejekontraktService.addLejekontrakt(lejekontrakt);
        return "redirect:/lejekontraktOverblik";
    }

    // TILFØJET: Generel updateLejekontrakt side (matcher menubar URL)
    @GetMapping("/updateLejekontrakt")
    public String updateLejekontraktOversigt(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        // Vis en tom form eller en liste til at vælge kontrakt at opdatere
        model.addAttribute("updateLejekontrakt", new Lejekontrakt());
        return "updateLejekontrakt";
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
        return "redirect:/lejekontraktOverblik";
    }

    @GetMapping("/slet-lejekontrakt/{id}")
    public String sletLejekontrakt(@PathVariable("id") int kontraktId)
    {
        lejekontraktService.deleteLejekontrakt(kontraktId);
        return "redirect:/lejekontraktOverblik";
    }

    //      ---- SKADERAPPORT ----
    @Autowired
    protected SkaderapportService skaderapportService;

    // RETTET: Matcher menubar URL "/skaderapport"
    @GetMapping("/skaderapport")
    public String visAlleRapporter(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            List<Skaderapport> rapporter = skaderapportService.getAllSkaderapporter();
            if (rapporter == null) rapporter = new ArrayList<>();
            model.addAttribute("rapporter", rapporter);
        } catch (Exception e) {
            System.out.println("Fejl i skaderapport: " + e.getMessage());
            model.addAttribute("rapporter", new ArrayList<>());
        }

        return "skaderapport";
    }

    // RETTET: Matcher menubar URL "/opretskaderapport"
    @GetMapping("/opretskaderapport")
    public String visOpretSkadeForm(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        model.addAttribute("skaderapport", new Skaderapport(0, 0, 0, 0, 0, 0, ""));
        return "opretSkaderapport";
    }

    // Gem skaderapport
    @PostMapping("/skaderapport/gem")
    public String opretSkaderapport(@ModelAttribute Skaderapport skaderapport)
    {
        skaderapportService.opretSkaderapport(skaderapport);
        return "redirect:/skaderapport";
    }

    // Slet skaderapport
    @PostMapping("/skaderapport/slet")
    public String sletSkaderapport(@RequestParam("id") int id)
    {
        skaderapportService.sletSkaderapport(id);
        return "redirect:/skaderapport";
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
        return "redirect:/skaderapport";
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

    // Vis liste over alle tilstandsrapporter (matcher menubar URL)
    @GetMapping("/tilstandsrapport")
    public String listTilstandsrapporter(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        try {
            List<Tilstandsrapport> rapporter = tilstandservice.getAllTilstandsrapporter();
            if (rapporter == null) rapporter = new ArrayList<>();
            model.addAttribute("rapporter", rapporter);
        } catch (Exception e) {
            System.out.println("Fejl i tilstandsrapport: " + e.getMessage());
            model.addAttribute("rapporter", new ArrayList<>());
        }

        return "tilstandsrapport";
    }

    // RETTET: Matcher menubar URL "/opretTilstandsrapport"
    @GetMapping("/opretTilstandsrapport")
    public String visOpretTilstandsForm(HttpSession session, Model model)
    {
        // Tjek om bruger er logget ind
        if (session.getAttribute("loggedIn") == null || !(boolean) session.getAttribute("loggedIn"))
        {
            return "redirect:/login";
        }

        model.addAttribute("rapport", new Tilstandsrapport());
        return "opretTilstandsrapport";
    }

    // Slet tilstandsrapport via id
    @GetMapping("/tilstandsrapport/slet/{id}")
    public String sletTilstandsrapport(@PathVariable("id") int id)
    {
        tilstandservice.deleteTilstandsrapport(id);
        return "redirect:/tilstandsrapport";
    }

}