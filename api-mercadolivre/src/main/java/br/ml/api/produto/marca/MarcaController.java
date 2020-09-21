package br.ml.api.produto.marca;

import java.io.IOException;
import java.util.Iterator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@RequestMapping(path = "/marcas")
public class MarcaController {
	
	@Autowired
	private MarcaService marcaService;
	
	@PostMapping()
	public Marca adicionar(@RequestBody @Valid Marca marca) {
			return marcaService.adicionar(marca);
	}

	/*@GetMapping(path = "/buscar")
	public Marca buscar( @RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "id", required = false) String id) {
		Marca marca = null;
		if (nome != null && id == null) {
			marca = marcaService.buscarPorNome(nome);
		} else if (id != null && nome == null) {
			marca = marcaService.buscarPorId(id);
		}
		return marca;

	}*/
	@GetMapping(path = "/nomes/{nome}")
	public Marca buscarPorNome(@PathVariable(value = "nome") String nome) {
		return marcaService.buscarPorNome(nome);
	}
	
	@GetMapping(path = "/{idMarca}")
	public Marca buscarPorId(@PathVariable(value = "idMarca") String idMarca) {
		System.out.println("marca "+idMarca);
		return marcaService.buscarPorId(idMarca);
	}
	
	@GetMapping(path = "/")
	public Iterator<Marca> buscarTodos() {
		return marcaService.buscarAll();
	}
	
	@GetMapping(path = "/todos")
	public Page<Marca> buscarAllPage (
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size){
		
		System.out.println("pag "+page+" "+size);
	    return this.marcaService.buscarAllPagination(page, size);
	 
	}
	@DeleteMapping(path = "/")
	public void excluirPorId(@RequestParam(value = "idMarca") String idMarca) throws IOException {
		marcaService.excluir(idMarca);
	}
}
