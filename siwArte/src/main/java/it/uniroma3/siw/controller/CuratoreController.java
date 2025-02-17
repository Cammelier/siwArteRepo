package it.uniroma3.siw.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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


	    @GetMapping("/curatori")
	    public String ShowCuratori(Model model) {
	        model.addAttribute("curatori", curatoreService.findAll());
	        return "curatori";
	    }

	    
	    @GetMapping("/dettagliCur/{id}")
	    public String getCuratore(@PathVariable("id") Long id, Model model) {
	        Curatore curatore = this.curatoreService.findById(id);
	        model.addAttribute("curatore", curatore);
	        return "dettagliCur";
	    }

	    
	   

	    
	  //managment admin
		@GetMapping(value = "/admin/managementCuratori")
		public String managemenCuratori(Model model) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());

				if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
					model.addAttribute("curatori", curatoreService.findAll());
					return "admin/managementCuratori";
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
										   @RequestParam("dataNascita") LocalDate dataNascita,
										   @RequestParam("luogoNascita") String luogoNascita,
										   @RequestParam("area") String area) {
		    Curatore existingCuratore = curatoreService.findById(id);
		    if (existingCuratore != null) {
		        existingCuratore.setNome(nome);
		        existingCuratore.setCognome(cognome);
		        existingCuratore.setCodiceFiscale(codiceFiscale);
		        existingCuratore.setDataNascita(dataNascita);
		        existingCuratore.setLuogoNascita(luogoNascita);
		        existingCuratore.setArea(area);
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
		public String saveCuratore(@RequestParam("nome") String nome,
		                           @RequestParam("cognome") String cognome,
		                           @RequestParam("dataNascita") LocalDate dataNascita,
		                           @RequestParam("codiceFiscale") String codiceFiscale,
		                           @RequestParam("luogoNascita") String luogoNascita,
		                           @RequestParam("area") String area) {
		    // 1️⃣ Creazione del curatore
		    Curatore curatore = new Curatore();
		    curatore.setNome(nome);
		    curatore.setCognome(cognome);
		    curatore.setCodiceFiscale(codiceFiscale);
		    curatore.setLuogoNascita(luogoNascita);
		    curatore.setDataNascita(dataNascita);
		    curatore.setArea(area);

		    // 🔹 Salva il curatore PRIMA di assegnargli le credenziali
		    curatore = curatoreService.saveCuratore(curatore);

		  

		    return "redirect:/admin/managementCuratori";
		}

		
		//delete curatore from admin
		@GetMapping("/admin/curatore/delete/{id}")
		public String deleteCuratore(@PathVariable("id") Long id) {
		    Curatore curatore = curatoreService.findById(id);
		        // Elimina il curatore
		        curatoreService.deleteCuratore(curatore);
		    return "redirect:/admin/managementCuratori"; // Reindirizza alla lista dei negozi
		}

}