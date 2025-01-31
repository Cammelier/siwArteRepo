package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.CuratoreService;

@Controller
public class AuthenticationController {

    @Autowired
    private CredenzialiService credenzialiService;

    @Autowired
    private CuratoreService curatoreService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String CURATORE_ROLE = "CURATORE";

    // Login
    @GetMapping(value = "/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping(value = "/success")
    public String successRedirect(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "index.html";
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());

            if (ADMIN_ROLE.equals(credenziali.getRuolo())) {
                return "admin/dashboard";
            }
            if (CURATORE_ROLE.equals(credenziali.getRuolo())) {
                return "curatore/dashboard";
            }
        }
        return "index";
    }

    // Registrazione Curatore
    @GetMapping(value = "/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("curatore", new Curatore());
        model.addAttribute("credenziali", new Credenziali());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registerCuratore(@RequestParam("nome") String nome,
                                   @RequestParam("cognome") String cognome,
                                   @RequestParam("dataNascita") String dataNascita,
                                   @RequestParam("luogoNascita") String luogoNascita,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   Model model) {
        try {
            // Creazione del curatore
            Curatore curatore = new Curatore();
            curatore.setNome(nome);
            curatore.setCognome(cognome);
            curatore.setDataNascita(LocalDate.parse(dataNascita));
            curatore.setLuogoNascita(luogoNascita);

            // Creazione delle credenziali
            Credenziali credenziali = new Credenziali();
            credenziali.setUsername(username);
            credenziali.setPassword(passwordEncoder.encode(password));
            credenziali.setRuolo(CURATORE_ROLE);
            credenziali.setCuratore(curatore);

            curatore.setCredenziali(credenziali);

            // Salvataggio
            curatoreService.saveCuratore(curatore);
            credenzialiService.save(credenziali);

            model.addAttribute("curatore", curatore);
            return "login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Errore durante la registrazione. Riprova.");
            return "register";
        }
    }

    // Home con gestione ruoli
    @GetMapping(value = "/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "index";
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());

            if (ADMIN_ROLE.equals(credenziali.getRuolo())) {
                return "admin/dashboard";
            }
            if (CURATORE_ROLE.equals(credenziali.getRuolo())) {
                return "curatore/dashboard";
            }
        }
        return "index";
    }
}
