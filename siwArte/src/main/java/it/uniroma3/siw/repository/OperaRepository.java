package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Opera;

@Repository
public interface OperaRepository extends CrudRepository<Opera, Long> {

    Optional<Opera> findByTitolo(String titolo);

    // ✅ Ottiene tutte le tecniche uniche presenti nel database
    @Query("SELECT DISTINCT o.tecnica FROM Opera o")
    List<String> findDistinctTecniche();

    // ✅ Ottiene tutti gli anni unici presenti nel database, ordinati in ordine crescente
    @Query("SELECT DISTINCT o.annoRealizzazione FROM Opera o ORDER BY o.annoRealizzazione")
    List<Integer> findDistinctAnni();
}
