package br.ml.api.buscas.mercadolivre;

import java.time.Instant;
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
@RequestMapping("/links-mercadolivre")
public class BuscasMercadoLivreController {

	@Autowired
	private BuscasMercadoLivreService searchPService;

	/**
	 * Adicionar um novo link de busca de produto
	 * @param searchProduto
	 * @return
	 */
	@PostMapping()
	public BuscasMercadoLivre adicionar(@RequestBody @Valid BuscasMercadoLivre searchProduto){
		System.out.println("value: "+searchProduto.getRangeFinal()+" "+searchProduto.getRangeInicial());
		return searchPService.adicionarLink(searchProduto); 
	}
	
	@PutMapping("/{idLinks}")
	public BuscasMercadoLivre atualizarProduto(@PathVariable(value = "idLinks") String idLinks, @RequestBody @Valid BuscasMercadoLivre searchProduto){
		System.out.println("itens "+ searchProduto.toString()+" id: "+idLinks);
		return searchPService.atualizar(idLinks, searchProduto); 
	}
	/**
	 * Buscar Todos
	 * @return
	 */
	@GetMapping("/")
	public Iterator<BuscasMercadoLivre> buscarTodos() {
		System.out.println("buscar todos");
		return searchPService.buscarTodos();
	}
	/**
	 * Buscar Todos
	 * @return
	 */
	@GetMapping("/status/{status}/errorlink/{errorlink}")
	public Iterator<BuscasMercadoLivre> buscarStatusAndErrorLinkTodos(@PathVariable(value = "status") boolean status, @PathVariable(value = "errorlink") boolean errorlink) {
		System.out.println("buscar todos "+ status+" "+errorlink);
		return searchPService.buscarStatusError(status, errorlink);
	}
	/**
	 * Busar passando o ID como parametro
	 * @param idSearch
	 * @return
	 */
	@GetMapping("/{idSearch}")
	public BuscasMercadoLivre buscarPorID(@PathVariable(value = "idSearch") String idSearch) {
		System.out.println("buscar id "+idSearch);
		return searchPService.buscarPorId(idSearch);
	}
	/**
	 * Atualizar um Link de busca
	 * @param idSearch
	 * @return
	 */
	@PutMapping("/{idSearch}")
	public BuscasMercadoLivre adicionar(@PathVariable(value = "idSearch") @Valid String idSearch){
		return null; 
	}
	/**
	 * Excluir
	 * @param idSearch
	 */
	@DeleteMapping("/{idSearch}")
	public void excluirPorId(@PathVariable(value = "idSearch") @Valid String idSearch) {
		searchPService.excluir(idSearch);		
	}
	
	@GetMapping(path = "/todos")
	public Page<BuscasMercadoLivre> buscarAllPage (
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size){
		
		System.out.println("pag "+page+" "+size);
	    return this.searchPService.buscarAllPagination(page, size);
	 
	}
	@GetMapping(path = "/tt")
	public Page<BuscasMercadoLivre> buscarTeste (
			@RequestParam boolean status,
			@RequestParam boolean error){
		Instant dateTime = Instant.now();
		System.out.println("pag "+status+" "+error+" "+dateTime);
	    return this.searchPService.buscarTodosParamStatusAndErrorLinkAndDataMenorOuIgual(status, error, dateTime.parse("2020-09-05T01:30:05.119655200Z"), 0);
	}
	
}
