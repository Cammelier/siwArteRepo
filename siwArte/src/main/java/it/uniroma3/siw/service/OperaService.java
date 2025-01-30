package it.uniroma3.siw.service;

import java.util.Optional;

import it.uniroma3.siw.model.Opera;
import it.uniroma3.siw.repository.OperaRepository;
import jakarta.transaction.Transactional;

public class OperaService {
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

    public Optional<Opera> getAuto(Long id) {
        return operaRepository.findById(id);
    }

    public Iterable<Opera> getAllAuto() {
        return operaRepository.findAll();
    }

    public void deleteOpera(Long id) {
        operaRepository.deleteById(id);
    }

    public void save(Opera opera) {
        operaRepository.save(opera);
    }

    public Opera findByName(String nome) {
        Optional<Opera> result = operaRepository.findByNome(nome);
        return result.orElse(null);
    }

    public void deleteOpera(Opera opera) {
        operaRepository.delete(opera);
    }
}
