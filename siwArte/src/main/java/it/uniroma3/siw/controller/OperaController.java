package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import it.uniroma3.siw.model.Opera;
import it.uniroma3.siw.service.PinacotecaService;

@Controller
public class OperaController {
	@Autowired
    private PinacotecaService service;

    @GetMapping
    public List<Opera> getOpere() {
        return service.getAllOpere();
    }

    @PostMapping
    public Opera createOpera(@RequestBody Opera opera) {
        return service.saveOpera(opera);
    }
}
