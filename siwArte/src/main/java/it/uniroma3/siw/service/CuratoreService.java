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

    
    
    public Curatore saveCuratore(Curatore curatore) {
        return curatoreRepository.save(curatore);
    }

   
    
    public Curatore findById(Long id) {
        return curatoreRepository.findById(id).orElse(null);
    }

    
    public List<Curatore> findAll() {
        return (List<Curatore>) curatoreRepository.findAll();
    }

    
    public void deleteCuratore(Curatore curatore) {
        curatoreRepository.delete(curatore);
    }

    
    public void deleteById(Long id) {
        curatoreRepository.deleteById(id);
    }
    
    public long getNumeroCuratori() {
    	return curatoreRepository.count();
    }
}
