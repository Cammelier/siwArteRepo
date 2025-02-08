package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Opera;
import it.uniroma3.siw.repository.OperaRepository;
import jakarta.transaction.Transactional;

@Service
public class OperaService {
	@Autowired
	private OperaRepository operaRepository;

    public Opera findById(Long id) {
        return operaRepository.findById(id).orElse(null);
    }

    public Iterable<Opera> findAll() {
        return operaRepository.findAll();
    }

    @Transactional
    public void saveOpera(Opera opera) {
        operaRepository.save(opera);
    }

    public Optional<Opera> getOpera(Long id) {
        return operaRepository.findById(id);
    }


    public void deleteOpera(Long id) {
        operaRepository.deleteById(id);
    }

    public void save(Opera opera) {
        operaRepository.save(opera);
    }

    public Opera findByName(String titolo) {
        Optional<Opera> result = operaRepository.findByTitolo(titolo);
        return result.orElse(null);
    }

    public void deleteOpera(Opera opera) {
        operaRepository.delete(opera);
    }

    public List<Integer> findAllAnni() {
        return operaRepository.findDistinctAnni();
    }
    public List<String> findAllTecniche() {
        return operaRepository.findDistinctTecniche();
    }


}
