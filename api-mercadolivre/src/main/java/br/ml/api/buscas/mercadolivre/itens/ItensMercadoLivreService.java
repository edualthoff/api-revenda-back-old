package br.ml.api.buscas.mercadolivre.itens;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.type.ApiNotFoundException;

@Service
public class ItensMercadoLivreService {
	private static final Logger log = LoggerFactory.getLogger(ItensMercadoLivreService.class);

	@Autowired
	private ItensMercadoLivreDAO itensMercadoLivreDAO;

	@Autowired
	private ElasticsearchOperations elkTemplate;

	public ItemMercadoLivre adicionar(ItemMercadoLivre itemMercadoLivre) {
		//System.out.println("ListaProduto adicionar " + itemMercadoLivre.getId() + " " + itemMercadoLivre.getTenantID());
		if (!(itensMercadoLivreDAO.existsById(itemMercadoLivre.getId()))) {
			return itensMercadoLivreDAO.save(itemMercadoLivre);
		}
		throw new ApiNotFoundException(
				ApiMessageSource.toMessageSetObject("objeto.add.error", "Adicionar Item - Mercadolivre -"));
	}

	public Iterable<ItemMercadoLivre> adicionarTodos(List<ItemMercadoLivre> itemMercadoLivre) {
		return itensMercadoLivreDAO.saveAll(itemMercadoLivre);
	}

	/**
	 * Update @param idUriBuscas @param tenantID
	 * @param List<ItemMercadoLivre> itemMercadoLivre
	 */
	public void atualizarTodos(List<ItemMercadoLivre> itemMercadoLivre) {
		log.debug("ListaProduto adicionar size: {}", itemMercadoLivre.size());
		List<UpdateQuery> querie = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).findAndRegisterModules();
		
		for (ItemMercadoLivre item : itemMercadoLivre) {
			item.setDate_modified(Instant.now());
			//System.out.println("item: "+item.getDate_created()+" "+item.getDate_modified()+" "+item.getId());
			@SuppressWarnings("unchecked")
			Map<String, Object> itensJsonMap = objectMapper.convertValue(item, Map.class);
			//System.out.println("dd: "+itensJsonMap.toString());
			UpdateQuery updateQuery = UpdateQuery.builder(item.getId()).withScriptedUpsert(true)
					.withUpsert(Document.from(itensJsonMap))
					//.withFetchSourceExcludes(Arrays.asList("date_created"))
					//.withFetchSource(true)
					.withScript("if (ctx._source != null){"
							+ "ctx._source.date_modified = params.date_modified;"
							+ "ctx._source.ativo = params.ativo;"
							+ "ctx._source.tipoAnuncio = params.tipoAnuncio;"
							+ "ctx._source.valor = params.valor;"
							+ "ctx._source.condicao = params.condicao;"
							+ "if(!ctx._source.tenantID.containsAll(params.tenantID)) {ctx._source.tenantID.addAll(params.tenantID);}"
							+ "if(!ctx._source.idUriBuscas.containsAll(params.idUriBuscas)) {ctx._source.idUriBuscas.addAll(params.idUriBuscas);}"
							+ "}")
					.withParams(itensJsonMap).build();
			querie.add(updateQuery);
		}
		elkTemplate.bulkUpdate(querie, ItemMercadoLivre.class);
	}

	public ItemMercadoLivre atualizar(String idBuscaList, ItemMercadoLivre itemMercadoLivre) {
		if (itensMercadoLivreDAO.existsById(idBuscaList)) {
			return itensMercadoLivreDAO.save(itemMercadoLivre);
		}
		throw new ApiNotFoundException(
				ApiMessageSource.toMessageSetObject("objeto.update.error", "Atualizar Item - Mercadolivre -"));
	}

	/**
	 * Buscar lista de Itens pelo id do produto do mercadolivre
	 * 
	 * @param idSearchProduto
	 * @return
	 */
	public Optional<ItemMercadoLivre> buscarPorId(String idItem) {
		return itensMercadoLivreDAO.findById(idItem);
	}

	/**
	 * Buscar Itens pelo id e pelo o status no caso se a varredura está ativa ou
	 * desativada True - ativo False - desativado
	 * 
	 * @param idSearchProduto
	 * @param status
	 * @return
	 */
	public List<ItemMercadoLivre> buscarPorIdUriBuscasAndStatus(String idBuscaList, boolean status) {
		log.debug("---- -- ListaProduto Comp value: " + idBuscaList + " " + status);
		return itensMercadoLivreDAO.findByIdUriBuscasAndAtivo(idBuscaList, status);
	}
}
