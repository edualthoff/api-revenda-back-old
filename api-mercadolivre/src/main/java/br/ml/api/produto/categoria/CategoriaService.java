package br.ml.api.produto.categoria;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.stereotype.Service;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.type.ApiBadRequestException;
import br.edx.exception.type.ApiNotFoundException;
import br.ml.api.config.elk.ElasticsearchIdentifierGenerator;
import br.ml.api.config.elk.IndexTenantDynamic;

@Service
public class CategoriaService implements Serializable {
	private static final long serialVersionUID = -6842496046095964846L;

	@Autowired
	private CategoriaDAO catDAO;
	@Autowired
	private ElasticsearchIdentifierGenerator elas;
	@Autowired
	private IndexTenantDynamic ind;

	
	/**
	 * Adciona uma Catategoria
	 * 
	 * @param catategoria
	 * @return
	 * @throws ApiNotFoundException
	 */
	public Categoria adicionar(Categoria categoria) throws ApiBadRequestException, NoSuchIndexException {
		try {
			if (catDAO.findByNome(categoria.getNome()).isEmpty()) {
				categoria.setId(elas.identifierId(Categoria.class));
				categoria.setTenantID(ind.getTenantID());
				return this.catDAO.save(categoria);
			}
			throw new ApiNotFoundException(ApiMessageSource.toMessageSetObject("objeto.add.error", "nome"));
		} catch (NoSuchIndexException e) {
			categoria.setId(elas.identifierId(Categoria.class));
			categoria.setTenantID(ind.getTenantID());
			return this.catDAO.save(categoria);
		}
	}

	/**
	 * Atualizar o Status de uma categoria
	 * 
	 * @param idCategoria
	 * @param status
	 * @return
	 */
	public Categoria atualizarStatus(String idCategoria, Categoria categoria) {
		Categoria cat = this.buscarPorId(idCategoria);
		if (categoria.getNome().equals(cat.getNome())) {
			cat.setStatus(categoria.isStatus());
			return this.catDAO.save(cat);
		}
		throw new ApiBadRequestException(ApiMessageSource.toMessageSetObject("objeto.update.error", "nome"));
	}

	/**
	 * Buscar todas as Catategoria
	 * 
	 * @return
	 */
	public Iterator<Categoria> buscarAll() {
		return catDAO.findAll().iterator();
	}

	/**
	 * Buscar Catategoria referente aos IDs informado
	 * 
	 * @param ids
	 * @return
	 */
	public Iterator<Categoria> buscarAllId(String[] ids) {
		return catDAO.findAllById(Arrays.asList(ids)).iterator();
	}

	/**
	 * Buscar todas as Categorias - Paginacao
	 * 
	 * @param pageable
	 * @return
	 */
	public Page<Categoria> buscarAllPagination(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("nome"));
		Page<Categoria> pageResult = catDAO.findAll(pageRequest);
		return new PageImpl<>(pageResult.toList(), pageRequest, pageResult.getTotalElements());
	}

	/**
	 * Excluir uma Catategoria
	 * 
	 * @param idCategoria
	 */
	public void excluir(String idCategoria) {
		catDAO.deleteById(idCategoria);
	}

	/**
	 * Buscar Catategoria por nome
	 * 
	 * @param nome
	 * @return
	 * @throws ApiNotFoundException
	 */
	public Categoria buscarPorNome(String nome) throws ApiNotFoundException {
		return catDAO.findByNome(nome).orElseThrow(() -> (new ApiNotFoundException(
				ApiMessageSource.toMessageSetObject("objeto.error.null.msg.object", "Categoria"))));
	}

	/**
	 * Buscar Catategoria por ID
	 * 
	 * @param id
	 * @return
	 * @throws ApiNotFoundException
	 */
	public Categoria buscarPorId(String id) throws ApiNotFoundException {
		return catDAO.findById(id).orElseThrow(() -> (new ApiNotFoundException(
				ApiMessageSource.toMessageSetObject("objeto.error.null.msg.object", "Categoria"))));
	}
}
