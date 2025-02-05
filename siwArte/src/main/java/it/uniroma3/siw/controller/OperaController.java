package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artista;

import it.uniroma3.siw.model.Credenziali;

import it.uniroma3.siw.model.Opera;
import it.uniroma3.siw.service.ArtistaService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.OperaService;
import jakarta.transaction.Transactional;

@Controller
public class OperaController {
	
	@Autowired
	private OperaService operaService;
	
	@Autowired
	private ArtistaService artistaService;

	@Autowired
	private CredenzialiService credenzialiService;
	
	
	@GetMapping("/opere")
	public String showOpera(Model model) {
		model.addAttribute("opere", operaService.findAll());
		return "opere.html"; // restituisce il nome della vista
	}
	
	@GetMapping("/dettagliOpera/{id}")
	public String getOpera(@PathVariable("id") Long id, Model model) {
	    Opera opera = this.operaService.findById(id); // Assicurati di avere un servizio BiciService per gestire le bici
	    model.addAttribute("opera", opera);
	    return "dettagliOpera"; // restituisce il nome della vista Thymeleaf
	}
	
	@Transactional
	@GetMapping("admin/opera/delete/{id}")
	public String deleteOperaAdmin(@PathVariable("id") Long id) {
	    Opera opera = operaService.findById(id);
	    if (opera != null) {
	        // Rimuovi il riferimento all'artista
	        Artista artista = opera.getArtista();
	        if (artista != null) {
	            artista.getOpera().remove(opera);
	            opera.setArtista(artista);
				artistaService.saveArtista(artista); // Salva le modifiche del negozio
	        }

	        // Cancella l'immagine associata
	        String immaginePath = "src/main/resources/static/uploads/opere/" + opera.getImmagine();
	        File immagineFile = new File(immaginePath);
	        if (immagineFile.exists()) {
	            immagineFile.delete();
	        }	     

	        operaService.deleteOpera(opera);
	    }
	    return "redirect:/admin/managementOpere"; // Reindirizza alla lista delle bici
	}
	
	@GetMapping(value = "/admin/managementOpere")
	public String managementOpere(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());

			if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
				model.addAttribute("opera", operaService.findAll());
				return "/admin/managementOpere";
			}
		}
		return "redirect:/";
	}
	
	@GetMapping("/formNewArtista")
	public String showFormNewArtista(@RequestParam(value = "artistaId", required = false) Long operaId, Model model, @RequestHeader(value = "referer", required = false) String referer) {
		if (operaId != null) {
			Artista artista = artistaService.findById(operaId);
			model.addAttribute("artista", artista);
		} else {
			Iterable<Artista> artisti = artistaService.getAllArtisti();
			model.addAttribute("artisti", artisti);
		}
		model.addAttribute("opera", new Opera());
		model.addAttribute("referer", referer);
		return "/formNewOpera";
	}
	
	@PostMapping("/formNewOpera")
	public String addOpera(@RequestParam("titolo") String titolo,
			@RequestParam("immagine") MultipartFile immagine,
			@RequestParam("artistaId") Long artistaId,
			@RequestParam("annoRealizzazione") int annorealizzazione,
			@RequestParam("tecnica") String tecnica,
			@RequestParam("referer") String referer) {
		Artista artista = artistaService.findById(artistaId);
		if (artista == null) {
			return "redirect:/admin/managementOpere";
		}

		Opera opera = new Opera();
		opera.setTitolo(titolo);
		opera.setTecnica(tecnica);
		opera.setAnnoRealizzazione(annorealizzazione);
		opera.setArtista(artista);

		// Gestisci l'immagine
		if (!immagine.isEmpty()) {
			try {
				String nuovoNomeImmagine = "uploads/opere/" + immagine.getOriginalFilename();

				File nuovoFileImmagineTemp = new File(System.getProperty("java.io.tmpdir") + "/" + nuovoNomeImmagine);

				// Assicurati che la directory esista
				File directoryTemp = nuovoFileImmagineTemp.getParentFile();
				if (!directoryTemp.exists()) {
					directoryTemp.mkdirs();
				}

				immagine.transferTo(nuovoFileImmagineTemp);

				// Copia l'immagine nella directory static
				File nuovoFileImmagine = new File("src/main/resources/static/" + nuovoNomeImmagine);
				File directory = nuovoFileImmagine.getParentFile();
				if (!directory.exists()) {
					directory.mkdirs();
				}
				Files.copy(nuovoFileImmagineTemp.toPath(), nuovoFileImmagine.toPath(), StandardCopyOption.REPLACE_EXISTING);

				opera.setImmagine(nuovoNomeImmagine);
			} catch (IOException e) {
				e.printStackTrace();
				return "redirect:/admin/managementOpere";
			}
		}
		
		
		operaService.saveOpera(opera);

		// Reindirizza alla pagina precedente
		return "redirect:" + referer;
	}
	@PostMapping("/editOpera")
	public String editBici(@RequestParam("id") Long id,
			@RequestParam("titolo") String titolo,
			@RequestParam("tecnica") String tecnica,
			@RequestParam("immagine") MultipartFile immagine,
			@RequestParam ("annoRealizzazione") int annoRealizzazione,
			@RequestParam("referer") String referer) {
		Opera opera = operaService.findById(id);
		if (opera == null) {
			return "redirect:/Opere";
		}

		opera.setTitolo(titolo);
		opera.setAnnoRealizzazione(annoRealizzazione);
		opera.setTecnica(tecnica);

		// Gestisci l'immagine
		if (!immagine.isEmpty()) {
			// Cancella la vecchia immagine
			String vecchiaImmagine = opera.getImmagine();
			if (vecchiaImmagine != null && !vecchiaImmagine.isEmpty()) {
				File fileVecchiaImmagine = new File("src/main/resources/static/" + vecchiaImmagine);
				if (fileVecchiaImmagine.exists()) {
					fileVecchiaImmagine.delete();
				}
			}
			// Salva la nuova immagine nella directory temporanea
			try {
				String nuovoNomeImmagine = "uploads/opere/" + immagine.getOriginalFilename();
				File nuovoFileImmagineTemp = new File(System.getProperty("java.io.tmpdir") + "/" + nuovoNomeImmagine);

				// Assicurati che la directory esista
				File directoryTemp = nuovoFileImmagineTemp.getParentFile();
				if (!directoryTemp.exists()) {
					directoryTemp.mkdirs();
				}

				immagine.transferTo(nuovoFileImmagineTemp);

				// Copia l'immagine nella directory static
				File nuovoFileImmagine = new File("src/main/resources/static/" + nuovoNomeImmagine);
				File directory = nuovoFileImmagine.getParentFile();
				if (!directory.exists()) {
					directory.mkdirs();
				}
				Files.copy(nuovoFileImmagineTemp.toPath(), nuovoFileImmagine.toPath(), StandardCopyOption.REPLACE_EXISTING);

				opera.setImmagine(nuovoNomeImmagine);
			} catch (IOException e) {
				e.printStackTrace();
				return "redirect:/opere";
			}
		}

		
		

		operaService.saveOpera(opera);

		// Reindirizza esclusivamente alla pagina precedente
		return "redirect:" + referer;
	}

}
