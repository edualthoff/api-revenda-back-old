package br.ml.api.produto.categoria;

import java.util.Iterator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaService catService;
	
	
	@PostMapping()
	public Categoria adicionar(@RequestBody @Valid Categoria categoria) {
		System.out.println("add "+categoria.getNome());
		return catService.adicionar(categoria);
		//throw new ApiNotFoundException(ApiMessageSource.toMessageSetObject("objeto.add.error", "Categoria")); 
	}

	@PutMapping(path = "/{idCategoria}")
	public Categoria atualizar(@PathVariable(value = "idCategoria") String idCategoria, @RequestBody @Valid Categoria categoria) {
		//System.out.println("atualizar "+idCategoria+" "+categoria.isStatus());
		return catService.atualizarStatus(idCategoria, categoria);
		//throw new ApiNotFoundException(ApiMessageSource.toMessageSetObject("objeto.add.error", "Categoria")); 
	}
	@GetMapping(path = "/nomes/{nome}")
	public Categoria buscarPorNome(@PathVariable(value = "nome") String nome) {
		return catService.buscarPorNome(nome);
	}
	
	@GetMapping(path = "/{idCategoria}")
	public Categoria buscarPorId(@PathVariable(value = "idCategoria") String idCategoria) {
		return catService.buscarPorId(idCategoria);
	}
	
	@GetMapping(path = "/")
	public Iterator<Categoria> buscarAll(@RequestParam(value = "ids", required = false) String[] ids) {
		if(ids != null) {
			return 	catService.buscarAllId(ids);
		}
		return catService.buscarAll();
	}
	
	@GetMapping(path = "/todos")
	public Page<Categoria> buscarAllPage (
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size){
		
		System.out.println("pag "+page+" "+size);
	    return this.catService.buscarAllPagination(page, size);
	 
	}
	
	@DeleteMapping(path = "/{idCategoria}")
	public void excluirId(@PathVariable(value = "idCategoria") String idCategoria) {
		System.out.println("cat: "+idCategoria);
		catService.excluir(idCategoria);
	}
}
