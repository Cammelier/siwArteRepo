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
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.CuratoreService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class AuthenticationController {

    @Autowired
    private CredenzialiService credenzialiService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

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
                return "admin/indexAdmin.html";
            }
            if (USER_ROLE.equals(credenziali.getRuolo())) {
                return "utente/indexUtente.html";
            }
        }
        return "index";
    }

    // Registrazione Utente
    @GetMapping(value = "/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Utente());
        model.addAttribute("credenziali", new Credenziali());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registerUtente(@RequestParam("nome") String nome,
                                   @RequestParam("cognome") String cognome,
                                   @RequestParam("dataNascita") String dataNascita,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   Model model) {
        try {
            // Creazione dell'utente
            Utente user = new Utente();
            user.setNome(nome);
            user.setCognome(cognome);
            user.setDataNascita(LocalDate.parse(dataNascita));
           

            // Creazione delle credenziali
            Credenziali credenziali = new Credenziali();
            credenziali.setUsername(username);
            credenziali.setPassword(passwordEncoder.encode(password));
            credenziali.setRuolo(USER_ROLE);
            credenziali.setUtente(user);

            user.setCredenziali(credenziali);

            
			// Salvataggio
            utenteService.saveUtente(user);
            credenzialiService.save(credenziali);

            model.addAttribute("user", user);
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
                return "admin/indexAdmin.html";
            }
            if (USER_ROLE.equals(credenziali.getRuolo())) {
                return "utente/index.html";
            }
        }
        return "index";
    }
}
