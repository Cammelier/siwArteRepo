package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.model.Opera;
import it.uniroma3.siw.repository.ArtistaRepository;
import it.uniroma3.siw.repository.CuratoreRepository;
import it.uniroma3.siw.repository.OperaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PinacotecaService {
	
	@Autowired
    private ArtistaRepository artistaRepo;

    @Autowired
    private OperaRepository operaRepo;

    @Autowired
    private CuratoreRepository curatoreRepo;

    // Artista methods
    public List<Artista> getAllArtisti() {
        List<Artista> artisti = new ArrayList<>();
        artistaRepo.findAll().forEach(artisti::add);
        return artisti;
    }

    public Artista saveArtista(Artista artista) {
        return artistaRepo.save(artista);
    }

    public void deleteArtista(Long id) {
        artistaRepo.deleteById(id);
    }

    // Opera methods
    public List<Opera> getAllOpere() {
        List<Opera> opere = new ArrayList<>();
        operaRepo.findAll().forEach(opere::add);
        return opere;
    }

    public Opera saveOpera(Opera opera) {
        return operaRepo.save(opera);
    }

    // Curatore methods
    public List<Curatore> getAllCuratori() {
        List<Curatore> curatori = new ArrayList<>();
        curatoreRepo.findAll().forEach(curatori::add);
        return curatori;
    }

    public Curatore saveCuratore(Curatore curatore) {
        return curatoreRepo.save(curatore);
    }
}
