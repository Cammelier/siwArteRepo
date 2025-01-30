package it.uniroma3.siw.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity	
public class Curatore {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nome;
	    private String cognome;
	    private String codiceFiscale;
	    private LocalDate dataNascita;
	    private String luogoNascita;
	    
	    @OneToOne
	    private Credenziali credenziali;
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getCognome() {
			return cognome;
		}
		public void setCognome(String cognome) {
			this.cognome = cognome;
		}
		public String getCodiceFiscale() {
			return codiceFiscale;
		}
		public void setCodiceFiscale(String codiceFiscale) {
			this.codiceFiscale = codiceFiscale;
		}
		public LocalDate getDataNascita() {
			return dataNascita;
		}
		public void setDataNascita(LocalDate dataNascita) {
			this.dataNascita = dataNascita;
		}
		public String getLuogoNascita() {
			return luogoNascita;
		}
		public void setLuogoNascita(String luogoNascita) {
			this.luogoNascita = luogoNascita;
		}
		public Credenziali getCredenziali() {
			return credenziali;
		}
		public void setCredenziali(Credenziali credenziali) {
			this.credenziali = credenziali;
		}
		
	    
}
