package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.service.PinacotecaService;


@Controller
@RequestMapping("/api/artisti")
public class ArtistaController {
	 	
		@Autowired
	    private PinacotecaService service;

	    @GetMapping
	    public List<Artista> getArtisti() {
	        return service.getAllArtisti();
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
}
