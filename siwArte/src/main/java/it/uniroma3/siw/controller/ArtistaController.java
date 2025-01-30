package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.service.ArtistaService;
import it.uniroma3.siw.service.PinacotecaService;


@Controller
@RequestMapping("/api/artisti")
public class ArtistaController {
	 	
		
		
		@Autowired
		private ArtistaService artistaService; 

		//view all
		@GetMapping("/artisti")
		public String showArtisti(Model model) {
			model.addAttribute("artisti", artistaService.getAllArtisti());
			return "artisti"; //restituisce il nome della vista
		}
		
	    
	    @GetMapping("/dettagliArt/{id}")
	    public String getArtista(@PathVariable("id") Long id, Model model) {
		    Artista artista= this.artistaService.findById(id);
		    model.addAttribute("artista", artista);
		    return "dettagliArt"; // restituisce il nome della vista Thymeleaf
		}

	    
	  //edit artista
		@GetMapping("/editArtista")
		public String showEditFormArt(@RequestParam("id") Long id, Model model) {
		    Artista artista = artistaService.findById(id);
		    if (artista == null) {
		        return "redirect:/admin/managementArtisti"; // Redirect if the negozio is not found
		    }
		    model.addAttribute("artista", artista);
		    return "/editArtista";
		}

		@PostMapping("/admin/updateArtista")
		public String updateArtista(@RequestParam("id") Long id,
		                                   @RequestParam("nome") String nome,
		                                   @RequestParam("cognome") String cognome,
		                                   @RequestParam("dataDiNascita") LocalDate dataDiNascita,
		                                   @RequestParam("luogoNascita") String luogoNascita,
		                                   @RequestParam("dataDiMore") String dataDiMorte)
											{
		    Artista existingArtista = artistaService.findById(id);
		    if (existingArtista != null) {
		        existingArtista.setNome(nome);
		        artistaService.saveArtista(existingArtista);
		    }
		    return "redirect:/admin/managementAristi";
		}
		
		//add artista from admin
		@GetMapping("/admin/formNewArtista")
		public String showFormNewArtista(Model model) {
			model.addAttribute("artista", new Artista());
			return "admin/formNewArtista";
		}
		
		@PostMapping("/admin/saveArtista")
		public String saveArtista(@RequestParam("nome") String nome,
				@RequestParam("cognome") String cognome,
				@RequestParam("dataDiNascita") LocalDate dataDiNascita,
				@RequestParam("luogodiNascita") String luogoDinascita,
				@RequestParam("dataDiMorte") LocalDate dataDiMorte) {
			Artista artista = new Artista();
			artista.setNome(nome);
			artista.setCognome(cognome);
			artista.setLuogoNascita(luogoDinascita);
			artista.setDataNascita(dataDiNascita);
			artista.setDataMorte(dataDiMorte);
			
			artistaService.saveArtista(artista);
			
			return "redirect:/admin/managementArtisti"; 
		}
		//delete artista from admin
		@GetMapping("/admin/artista/delete/{id}")
		public String deleteArtista(@PathVariable("id") Long id) {
		    Artista artista = artistaService.findById(id);
		    if (artista != null) {
		        // Elimina l' artista
		        artistaService.deleteArtista(artista);
		    }
		    return "redirect:/admin/managementArtisti"; // Reindirizza alla lista dei negozi
		}
}
