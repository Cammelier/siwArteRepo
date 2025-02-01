package it.uniroma3.siw.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.CuratoreService;


@Controller
public class CuratoreController {
	 
	 @Autowired
	 	private CuratoreService curatoreService;
	 @Autowired
		private CredenzialiService credenzialiService;

	@Autowired
		private PasswordEncoder passwordEncoder;


	    @GetMapping("/curatori")
	    public String ShowCuratori(Model model) {
	        model.addAttribute("curatore",curatoreService.findAll());
	        return "curatori";
	    }

	    
	    @GetMapping("/dettagliCur/{id}")
		public String getCuratore(@PathVariable("id") Long id, Model model) {
		    Curatore curatore = this.curatoreService.findById(id);
		    model.addAttribute("curatore", curatore);
		    return "dettagliCur"; // restituisce il nome della vista Thymeleaf
		}
	    
	  //managment admin
		@GetMapping(value = "/admin/managementCuratori")
		public String managementNegozi(Model model) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());

				if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
					model.addAttribute("curatore", curatoreService.findAll());
					return "admin/managementCuratori.html";
				}
			}
			return "redirect:/";
		}
	    
	  //edit curatore
		@GetMapping("/editCuratore")
		public String showEditFormCur(@RequestParam("id") Long id, Model model) {
		    Curatore curatore = curatoreService.findById(id);
		    if (curatore == null) {
		        return "redirect:/admin/managementCuratori"; // Redirect if the curatore is not found
		    }
		    model.addAttribute("curatore", curatore);
		    return "/editCuratore";
		}
		
		@PostMapping("/admin/updateCuratore")
		public String updateCuratore(@RequestParam("id") Long id,
		                                   @RequestParam("nome") String nome,
		                                   @RequestParam("cognome") String cognome,
		                                   @RequestParam("codiceFiscale") String codiceFiscale,
										   @RequestParam("dataDiNascita") LocalDate dataDiNascita,
										   @RequestParam("luogoDinascita") String luogoDinascita) {
		    Curatore existingCuratore = curatoreService.findById(id);
		    if (existingCuratore != null) {
		        existingCuratore.setNome(nome);
		        existingCuratore.setCognome(cognome);
		        existingCuratore.setCodiceFiscale(codiceFiscale);
		        existingCuratore.setDataNascita(dataDiNascita);
		        existingCuratore.setLuogoNascita(luogoDinascita);
		    curatoreService.saveCuratore(existingCuratore);
		}
		return "redirect:/admin/managementCuratori";
	}
		//add curatore from admin
		@GetMapping("/admin/formNewCuratore")
		public String showFormNewCuratore(Model model) {
			model.addAttribute("curatore", new Curatore());
			return "admin/formNewCuratore";
		}
		
		@PostMapping("/admin/saveCuratore")
		public String saveNegozio(@RequestParam("nome") String nome,
				@RequestParam("cognome") String cognome,
				@RequestParam("dataDiNascita") LocalDate dataDiNascita,
				@RequestParam("codiceFiscale") String codiceFiscale,
				@RequestParam("luogoDinascita") String luogoDiNascita,
				@RequestParam("username") String username,
				@RequestParam("password") String password) {
			Curatore curatore = new Curatore();
			curatore.setNome(nome);
			curatore.setCognome(cognome);
			curatore.setCodiceFiscale(codiceFiscale);
			curatore.setLuogoNascita(luogoDiNascita);
			curatore.setDataNascita(dataDiNascita);
			
			Credenziali credenziali = new Credenziali();
			credenziali.setUsername(username);
			credenziali.setPassword(passwordEncoder.encode(password));
			credenziali.setRuolo(Credenziali.CURATORE_ROLE);
			credenziali.setCuratore(curatore);
			curatore.setCredenziali(credenziali);
			
			credenzialiService.save(credenziali);
			
			curatoreService.saveCuratore(curatore);
			
			return "redirect:/admin/managementcuratori";
		}
		
		//delete curatore from admin
		@GetMapping("/admin/curatore/delete/{id}")
		public String deleteCuratore(@PathVariable("id") Long id) {
		    Curatore curatore = curatoreService.findById(id);
		    if (curatore != null) {
		    	Credenziali credenziali = curatore.getCredenziali();
		    	if(credenziali != null) {
		    		curatore.setCredenziali(null);
		    		curatoreService.saveCuratore(curatore);
		    		
		    		credenziali.setCuratore(null);
		    		credenzialiService.save(credenziali);
		    		
		    		credenzialiService.delete(credenziali);
		    	}
		        // Elimina il curatore
		        curatoreService.deleteCuratore(curatore);
		    }
		    return "redirect:/admin/managementCuratori"; // Reindirizza alla lista dei negozi
		}

}