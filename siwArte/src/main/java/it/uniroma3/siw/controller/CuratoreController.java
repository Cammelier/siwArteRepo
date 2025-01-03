package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.service.PinacotecaService;

@Controller
public class CuratoreController {
	 @Autowired
	    private PinacotecaService service;

	    @GetMapping
	    public List<Curatore> getCuratori() {
	        return service.getAllCuratori();
	    }

	    @PostMapping
	    public Curatore createCuratore( @RequestBody Curatore curatore) {
	        return service.saveCuratore(curatore);
	    }
}
