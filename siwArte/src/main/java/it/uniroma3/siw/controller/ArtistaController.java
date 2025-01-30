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
import it.uniroma3.siw.service.ArtistaService;
import it.uniroma3.siw.service.PinacotecaService;


@Controller
@RequestMapping("/api/artisti")
public class ArtistaController {
	 	
		@Autowired
	    private PinacotecaService service;
		
		@Autowired
		private ArtistaService artistaService; 

	    @GetMapping("/artisti")
	    public List<Artista> getArtisti() {
	        return service.getAllArtisti();
	    }
	    
	    @GetMapping("/dettagliArt/{id}")
	    public String getArtista(@PathVariable("id") Long id, Model model) {
		    Artista artista= this.artistaService.findById(id);
		    model.addAttribute("artista", artista);
		    return "dettagliArt"; // restituisce il nome della vista Thymeleaf
		}


	    @PostMapping
	    public Artista createArtista( @RequestBody Artista artista) {
	        return service.saveArtista(artista);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteArtista(@PathVariable Long id) {
	        service.deleteArtista(id);
	        return ResponseEntity.noContent().build();
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
		public String updateNegozio(@RequestParam("id") Long id,
		                                   @RequestParam("nome") String nome,
		                                   @RequestParam("cognome") String cognome,
		                                   @RequestParam("dataDiNascita") LocalDate dataDiNascita,
		                                   @RequestParam("luogoNascita") String luogoNascita,
		                                   @RequestParam("dataDiMore") String dataDiMorte)
											{
		    Artista existingArtista = artistaService.findById(id);
		    if (existingArtista != null) {
		        existingArtista.setNome(nome);
		        artistaService.save(existingArtista);
		    }
		    return "redirect:/admin/managementAristi";
		}
}
