package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Artista {
	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nome;
	    private String cognome;
	    private LocalDate dataNascita;
	    private String luogoNascita;
	    private LocalDate dataMorte;
	    
	    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Opera> opera = new ArrayList<>();
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
		public LocalDate getDataNascita() {
			return dataNascita;
		}
		public List<Opera> getOpera() {
			return opera;
		}


		public void setOpere(List<Opera> opera) {
			this.opera = opera;
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
		public LocalDate getDataMorte() {
			return dataMorte;
		}
		public void setDataMorte(LocalDate dataMorte) {
			this.dataMorte = dataMorte;
		}

}
