package br.ml.api.produto.item;

import java.util.Iterator;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController()
@RequestMapping(path = "/itens")
public class ItemProdutoController {

	@Autowired
	private ItemProdutoService itensService;
	
	@PostMapping()
	public ItemProduto adicionarProduto(@RequestBody @Valid ItemProduto itemProduto){
		System.out.println("itens "+ itemProduto.toString());
		return itensService.adicionar(itemProduto); 
	}
	@PutMapping("/{idItens}")
	public ItemProduto atualizarProduto(@PathVariable(value = "idItens") String idItens, @RequestBody @Valid ItemProduto itemProduto){
		System.out.println("itens "+ itemProduto.toString()+" id: "+idItens);
		return itensService.atualizar(idItens, itemProduto); 
	}
	
	@GetMapping("/categorias/{idCategoria}")
	public Iterator<ItemProduto> buscarTodosPorCategoriaId(@PathVariable(value = "idCategoria") String idCategoria){
		return itensService.buscarAllPorCategoria(idCategoria);
	}
	
	@GetMapping("/marcas/{idMarca}")
	public Iterator<ItemProduto> buscarTodosPorMarcaId(@PathVariable(value = "idMarca") String idMarca){
		return itensService.buscarAllPorMarca(idMarca);
	}
	
	@GetMapping("/")
	public Iterator<ItemProduto> buscarTodos(@PathParam(value = "idMarca") String idMarca){
		return itensService.buscarAll();
	}
	
	@GetMapping("/{idItens}")
	public ItemProduto buscarPorId(@PathVariable(value = "idItens") String idItens){
		return itensService.buscarItensPorId(idItens);
	}
	
	@GetMapping(path = "/modelo")
	public Page<ItemProduto> buscarModeloAllPageAutoComplete (
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "modelo", required = true) String modelo){
		
		System.out.println("pag "+page+" "+size+" modelo: "+modelo);
	    return this.itensService.buscarModeloAllPaginationAutoComplete(page, size, modelo);
	}
	
	@GetMapping(path = "/todos")
	public Page<ItemProduto> buscarAllPage (
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size){
		
		System.out.println("pag "+page+" "+size);
	    return this.itensService.buscarAllPagination(page, size);
	 
	}
	
	@DeleteMapping("/{idCategoria}")
	public void deletarUmaCategoria(@PathParam(value = "idCategoria") String idCategoria) {
		
	}
}
