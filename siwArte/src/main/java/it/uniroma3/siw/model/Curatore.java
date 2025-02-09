package it.uniroma3.siw.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	    private String area;
	    private String immagine;
	    
	   
	    
		public String getImmagine() {
			return immagine;
		}
		public void setImmagine(String immagine) {
			this.immagine = immagine;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
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
		
		
	    
}
