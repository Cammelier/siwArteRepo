package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import it.uniroma3.siw.service.CredenzialiService;



@Controller

public class ArtistaController {
	 	
		
		
		@Autowired
		private ArtistaService artistaService; 
		
		@Autowired
		private CredenzialiService credenzialiService;

		//view all
		@GetMapping("/artisti")
		public String showArtisti(Model model) {
			model.addAttribute("artisti", artistaService.findAll());
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
		@GetMapping(value = "/admin/managementArtisti")
		public String managementArtisti(Model model) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());

				if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
					model.addAttribute("artisti", artistaService.findAll());
					return "/admin/managementArtisti.html";
				}
			}
			return "redirect:/";
		}

		@PostMapping("/admin/updateArtista")
		public String updateArtista(@RequestParam("id") Long id,
		                                   @RequestParam("nome") String nome,
		                                   @RequestParam("cognome") String cognome,
		                                   @RequestParam("dataNascita") LocalDate dataNascita,
		                                   @RequestParam("luogoNascita") String luogoNascita,
		                                   @RequestParam("dataMorte") LocalDate dataMorte,
		                                   @RequestParam("immagine")MultipartFile immagine)
											{
		    Artista existingArtista = artistaService.findById(id);
		    if (existingArtista != null) {
		        existingArtista.setNome(nome);
		        artistaService.saveArtista(existingArtista);
		        
		     // Gestisci l'immagine
		        if (!immagine.isEmpty()) {
		            // Cancella la vecchia immagine
		            String vecchiaImmagine = existingArtista.getImmagine();
		            if (vecchiaImmagine != null && !vecchiaImmagine.isEmpty()) {
		                File fileVecchiaImmagine = new File("src/main/resources/static/" + vecchiaImmagine);
		                if (fileVecchiaImmagine.exists()) {
		                    fileVecchiaImmagine.delete();
		                }
		            }
		            // Salva la nuova immagine nella directory temporanea
		            try {
		                String nuovoNomeImmagine = "/uploads/artisti/" + immagine.getOriginalFilename();
		                File nuovoFileImmagineTemp = new File(System.getProperty("java.io.tmpdir") + "/" + nuovoNomeImmagine);

		                // Assicurati che la directory esista
		                File directoryTemp = nuovoFileImmagineTemp.getParentFile();
		                if (!directoryTemp.exists()) {
		                    directoryTemp.mkdirs();
		                }

		                immagine.transferTo(nuovoFileImmagineTemp);

		                // Copia l'immagine nella directory static
		                File nuovoFileImmagine = new File("src/main/resources/static" + nuovoNomeImmagine);
		                File directory = nuovoFileImmagine.getParentFile();
		                if (!directory.exists()) {
		                    directory.mkdirs();
		                }
		                Files.copy(nuovoFileImmagineTemp.toPath(), nuovoFileImmagine.toPath(), StandardCopyOption.REPLACE_EXISTING);

		                existingArtista.setImmagine(nuovoNomeImmagine);
		            } catch (IOException e) {
		                e.printStackTrace();
		                return "redirect:/admin/managementArtisti";
		            }
		        }
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
				@RequestParam("dataNascita") LocalDate dataNascita,
				@RequestParam("luogoNascita") String luogoNascita,
				@RequestParam("dataMorte") LocalDate dataMorte,
				@RequestParam("immagine")MultipartFile immagine) {
			Artista artista = new Artista();
			artista.setNome(nome);
			artista.setCognome(cognome);
			artista.setLuogoNascita(luogoNascita);
			artista.setDataNascita(dataNascita);
			artista.setDataMorte(dataMorte);
			if (!immagine.isEmpty()) {
				try {
					String nuovoNomeImmagine = "/uploads/artisti/" + immagine.getOriginalFilename();
					File nuovoFileImmagineTemp = new File(System.getProperty("java.io.tmpdir") + "/" + nuovoNomeImmagine);

					// Assicurati che la directory esista
					File directoryTemp = nuovoFileImmagineTemp.getParentFile();
					if (!directoryTemp.exists()) {
						directoryTemp.mkdirs();
					}

					immagine.transferTo(nuovoFileImmagineTemp);

					// Copia l'immagine nella directory static
					File nuovoFileImmagine = new File("src/main/resources/static" + nuovoNomeImmagine);
					File directory = nuovoFileImmagine.getParentFile();
					if (!directory.exists()) {
						directory.mkdirs();
					}
					Files.copy(nuovoFileImmagineTemp.toPath(), nuovoFileImmagine.toPath(), StandardCopyOption.REPLACE_EXISTING);

					artista.setImmagine(nuovoNomeImmagine);
				} catch (IOException e) {
					e.printStackTrace();
					return "redirect:/admin/formNewArtista";
				}
			}
			artistaService.saveArtista(artista);
			
			return "redirect:/admin/managementArtisti"; 
		}
		//delete artista from admin
		@GetMapping("/admin/artista/delete/{id}")
		public String deleteArtista(@PathVariable("id") Long id) {
			 Artista artista = artistaService.findById(id);
			    if (artista != null) {

			        // Cancella l'immagine associata
			        String vecchiaImmagine = artista.getImmagine();
			        if (vecchiaImmagine != null && !vecchiaImmagine.isEmpty()) {
			            File immagineFile = new File("src/main/resources/static" + vecchiaImmagine);
			            if (immagineFile.exists()) {
			                immagineFile.delete();
			            }
			        }

			        // Elimina il negozio
			        artistaService.deleteArtista(artista);
			    }
			    return "redirect:/admin/managementArtisti"; // Reindirizza alla lista dei negozi
			}
}
