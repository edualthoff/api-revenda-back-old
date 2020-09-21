package br.ml.api.buscas.mercadolivre;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.type.ApiBadRequestException;
import br.edx.exception.type.ApiNotFoundException;
import br.ml.api.config.elk.ElasticsearchIdentifierGenerator;
import br.ml.api.config.elk.IndexTenantDynamic;

@Service
public class BuscasMercadoLivreService {

	@Autowired private BuscasMercadoLivreDAO searchDao;
	@Autowired private ElasticsearchIdentifierGenerator elastic;
	@Autowired private IndexTenantDynamic index;
	@Autowired
	private ElasticsearchOperations elkTemplate;
	/**
	 * Adicionar uma busca de produto automatica
	 * 
	 * @param buscasMercadoLivre
	 * @return
	 * @throws ApiNotFoundException
	 */
	public BuscasMercadoLivre adicionarLink(BuscasMercadoLivre buscasMercadoLivre) throws ApiNotFoundException, NoSuchIndexException {

		buscasMercadoLivre.setId(elastic.identifierId(BuscasMercadoLivre.class));
		buscasMercadoLivre.setTenantID(index.getTenantID());
		buscasMercadoLivre.getSchedulingTime().setNextDate(buscasMercadoLivre.getSchedulingTime().nextDateUpdateOrCreate());
		try {
			return searchDao.save(new ValidarCamposBuscas(buscasMercadoLivre).build());
		} catch (NoSuchIndexException e) {
			return this.searchDao.save(new ValidarCamposBuscas(buscasMercadoLivre).build());
		}
		// throw new ApiNotFoundException(ApiMessageSource.toMessageSetObject("objeto.add.error", "links Mercado Livre""));
	}

	/**
	 * Atualizar todos simultaneno, passando uma lista de Buscas Mercado Livre
	 * @param listBuscasMercadoLivre
	 */
	public void atualizarTodos(List<BuscasMercadoLivre> listBuscasMercadoLivre) {
		this.searchDao.saveAll(listBuscasMercadoLivre);
	}
	
	/**
	 * Atualizar Busca de produto automatica
	 * 
	 * @param buscasMercadoLivre
	 * @param idLinkML
	 * @return
	 */
	public BuscasMercadoLivre atualizar(String idLinkML, BuscasMercadoLivre buscasMercadoLivre) {
		BuscasMercadoLivre sp = this.buscarPorId(idLinkML);
		if (buscasMercadoLivre.getId().equals(sp.getId())) {
			//searchProduto.setId(elastic.identifierId(BuscasMercadoLivre.class));
			//searchProduto.setTenantID(index.getTenantID());
			buscasMercadoLivre.setId(sp.getId());
			buscasMercadoLivre.setTenantID(sp.getTenantID());
			return this.searchDao.save(new ValidarCamposBuscas(buscasMercadoLivre).build());
		}
		throw new ApiBadRequestException(ApiMessageSource.toMessageSetObject("objeto.update.error", "nome"));
	}

	/**
	 * Buscar todas as Categorias - Paginacao
	 * 
	 * @param pageable
	 * @return
	 */
	public Page<BuscasMercadoLivre> buscarAllPagination(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<BuscasMercadoLivre> pageResult = searchDao.findAll(pageRequest);
		return new PageImpl<>(pageResult.toList(), pageRequest, pageResult.getTotalElements());
	}

	/**
	 * Buscar todas as buscas automaticas de produtos cadastrado
	 * 
	 * @return
	 */
	public Iterator<BuscasMercadoLivre> buscarTodos() {
		return searchDao.findAll().iterator();
	}
	
	/**
	 * Buscas todos por status @param boolean true ou false
	 * @param status
	 * @return
	 */
	public Iterator<BuscasMercadoLivre> buscarStatusTodos(boolean status) {
		return searchDao.findByStatus(status).iterator();
	}
	/**
	 * Buscas todos por status @param boolean true ou false
	 * @param status
	 * @return
	 */
	public List<BuscasMercadoLivre> buscarStatusAndErrorLinkTodos(boolean status, boolean errorLink) {
		return searchDao.findByStatusAndErrorLink(status, errorLink);
	}
	/**
	 * 
	 * @param status
	 * @param errorLink
	 * @param dateTime
	 * @return List<BuscasMercadoLivre>
	 */
	public Page<BuscasMercadoLivre> buscarTodosParamStatusAndErrorLinkAndDataMenorOuIgual(boolean status, boolean errorLink, Instant dateTime, int page ) {
		QueryBuilder qb = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("status", true)).must(QueryBuilders.matchQuery("errorLink", false))
				.filter(QueryBuilders.nestedQuery("schedulingTime", QueryBuilders.rangeQuery("schedulingTime.nextDate").lte(dateTime), ScoreMode.Max));
		
		PageRequest pageRequest = PageRequest.of(page, 30);
		NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(qb).withPageable(PageRequest.of(page, 30)).build();
		SearchHitsIterator<BuscasMercadoLivre> stream = elkTemplate.searchForStream(nativeSearchQuery, BuscasMercadoLivre.class);
		List<BuscasMercadoLivre> bmlist = new ArrayList<>();
		while (stream.hasNext()) {
			bmlist.add(stream.next().getContent());
		}
		stream.close();
		//return new PageImpl<>(bmlist);
		return searchDao.findBySchedulingTimeNextDateLessThanEqual(status, errorLink, dateTime, pageRequest);
	}
	
	public Iterator<BuscasMercadoLivre> buscarStatusError(boolean status, boolean errorLink) {
		return searchDao.findByStatusAndErrorLink(status, errorLink).iterator();
	}
	
	public BuscasMercadoLivre buscarPorId(String idSearch) {
		return searchDao.findById(idSearch).orElseThrow(() -> (new ApiNotFoundException(
				ApiMessageSource.toMessageSetObject("objeto.error.null.msg.object", "Link Produto"))));
	}

	/**
	 * Exluir uma uma busca de produto automatica
	 * 
	 * @param searchId
	 */
	public void excluir(String searchId) {
		searchDao.deleteById(searchId);
	}
}
