package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Curatore;
@Repository
public interface CuratoreRepository extends CrudRepository <Curatore,Long> {
}
