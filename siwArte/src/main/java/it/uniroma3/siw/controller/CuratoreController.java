package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.model.Negozio;
import it.uniroma3.siw.service.CuratoreService;
import it.uniroma3.siw.service.PinacotecaService;

@Controller
public class CuratoreController {
	 @Autowired
	    private PinacotecaService service;
	 @Autowired
	 	private CuratoreService curatoreService;

	    @GetMapping("/curatori")
	    public String ShowCuratori(Model model) {
	        model.addAttribute("curatore",curatoreService.findAll());
	        return "curatori";
	    }

	    @PostMapping
	    public Curatore createCuratore( @RequestBody Curatore curatore) {
	        return service.saveCuratore(curatore);
	    }
	    
	    @GetMapping("/dettagliCur/{id}")
		public String getCuratore(@PathVariable("id") Long id, Model model) {
		    Curatore curatore = this.curatoreService.findById(id);
		    model.addAttribute("curatore", curatore);
		    return "dettagliCur"; // restituisce il nome della vista Thymeleaf
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
		
		@PostMapping("/admin/updateNegozio")
		public String updateNegozio(@RequestParam("id") Long id,
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
				@RequestParam("luogoDinascita") String luogoDiNascita) {
			Curatore curatore = new Curatore();
			curatore.setNome(nome);
			curatore.setCognome(cognome);
			curatore.setCodiceFiscale(codiceFiscale);
			curatore.setLuogoNascita(luogoDiNascita);
			curatore.setDataNascita(dataDiNascita);
			
			curatoreService.saveCuratore(curatore);
			
			return "redirect:/admin/managementcuratori";
		}
}