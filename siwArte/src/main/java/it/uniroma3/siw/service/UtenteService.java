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
	    
	    @Transactional
	    public Utente createUser(String nome, String cognome, String email, String username, String password) {
	        // 1️⃣ Crea l'utente
	        Utente user = new Utente();
	        user.setNome(nome);
	        user.setCognome(cognome);
	        user.setEmail(email);

	        // 🔹 Salva l'utente prima di creare le credenziali
	        user = userRepository.save(user);

	        // 2️⃣ Crea le credenziali
	        Credenziali credenziali = new Credenziali();
	        credenziali.setUsername(username);
	        credenziali.setPassword(passwordEncoder.encode(password)); // Crittografa password
	        credenziali.setRuolo(Credenziali.USER_ROLE);
	        credenziali.setUtente(user);

	        // 3️⃣ Collega credenziali all'utente
	        user.setCredenziali(credenziali);

	        // 4️⃣ Salva le credenziali
	        credenzialiRepository.save(credenziali);

	        return user;
	    }

	}


