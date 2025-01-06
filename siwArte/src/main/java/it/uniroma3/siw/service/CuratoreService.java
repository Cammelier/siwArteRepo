package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.repository.CuratoreRepository;

@Service
public class CuratoreService {

    @Autowired
    private CuratoreRepository curatoreRepository;

    /** 
     * Salva un curatore nel database.
     * @param curatore il curatore da salvare
     * @return il curatore salvato
     */
    @Transactional
    public Curatore save(Curatore curatore) {
        return curatoreRepository.save(curatore);
    }

    /**
     * Trova un curatore per ID.
     * @param id l'ID del curatore
     * @return un Optional contenente il curatore, se esiste
     */
    @Transactional(readOnly = true)
    public Optional<Curatore> findById(Long id) {
        return curatoreRepository.findById(id);
    }

    /**
     * Ritorna tutti i curatori presenti nel database.
     * @return una lista di curatori
     */
    @Transactional(readOnly = true)
    public List<Curatore> findAll() {
        return (List<Curatore>) curatoreRepository.findAll();
    }

    /**
     * Elimina un curatore dal database.
     * @param curatore il curatore da eliminare
     */
    @Transactional
    public void delete(Curatore curatore) {
        curatoreRepository.delete(curatore);
    }

    /**
     * Elimina un curatore in base all'ID.
     * @param id l'ID del curatore da eliminare
     */
    @Transactional
    public void deleteById(Long id) {
        curatoreRepository.deleteById(id);
    }
}
