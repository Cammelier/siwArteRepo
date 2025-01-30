package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.repository.ArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    // Recupera tutti gli artisti
    public List<Artista> getAllArtisti() {
        return (List<Artista>) artistaRepository.findAll();
    }

    // Recupera un artista per ID
    public Optional<Artista> getArtistaById(Long id) {
        return artistaRepository.findById(id);
    }

    // Aggiunge o aggiorna un artista
    public Artista saveOrUpdateArtista(Artista artista) {
        return artistaRepository.save(artista);
    }

    // Elimina un artista per ID
    public void deleteArtista(Long id) {
        artistaRepository.deleteById(id);
    }
    
    public Artista findById(Long id) {
    	return artistaRepository.findById(id).orElse(null);
    }
    
    public void saveArtista(Artista artista) {
    	artistaRepository.save(artista);
    }
    
    public void deleteArtista(Artista artista) {
    	 artistaRepository.delete(artista);
    }
}
