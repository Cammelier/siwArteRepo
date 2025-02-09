package it.uniroma3.siw.service;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.crypto.password.PasswordEncoder;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.model.Utente;
	import it.uniroma3.siw.repository.CredenzialiRepository;
	import it.uniroma3.siw.repository.UtenteRepository;
@Service
public class UtenteService {
	
	

	    @Autowired
	    private UtenteRepository userRepository;

	    @Autowired
	    private CredenzialiRepository credenzialiRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    public Utente saveUtente(Utente user) {
	        return userRepository.save(user);
	    }
	    
	   
	    

	}


