package it.uniroma3.siw.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
@Entity
public class Utente {
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    private String nome;
	    private String cognome;
	    private String email;
	    private LocalDate dataNascita;

	    @OneToOne(mappedBy = "utente", cascade = CascadeType.ALL)
	    private Credenziali credenziali;

	    // Getters e Setters
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }

	    public String getNome() { return nome; }
	    public void setNome(String nome) { this.nome = nome; }

	    public String getCognome() { return cognome; }
	    public void setCognome(String cognome) { this.cognome = cognome; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public LocalDate getDataNascita() { return dataNascita; }
	    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

	    public Credenziali getCredenziali() { return credenziali; }
	    public void setCredenziali(Credenziali credenziali) { this.credenziali = credenziali; }
}	

