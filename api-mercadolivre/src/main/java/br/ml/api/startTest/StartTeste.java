package br.ml.api.startTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/teste-api")
public class StartTeste {

	@GetMapping("/")
	public String name() {
		return "Iniciado com Sucesso";
	}
}
