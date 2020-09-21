package br.ml.api.produto.marca;

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
import br.edx.exception.type.ApiNotFoundException;
import br.ml.api.config.elk.ElasticsearchIdentifierGenerator;
import br.ml.api.config.elk.IndexTenantDynamic;

@Service
public class MarcaService implements Serializable {
	private static final long serialVersionUID = 828201954167452140L;

	@Autowired
	private MarcaDAO marcaDAO;
	@Autowired
	private ElasticsearchIdentifierGenerator elastic;
	@Autowired
	private IndexTenantDynamic index;
	
	
	/**
	 * Adicionar uma marca
	 * 
	 * @param marca
	 * @return
	 * @throws ApiNotFoundException
	 */
	public Marca adicionar(Marca marca) throws ApiNotFoundException {
		try {
			if (marcaDAO.findByNome(marca.getNome()).isEmpty()) {
				marca.setId(elastic.identifierId(Marca.class));
				marca.setTenantID(index.getTenantID());
				return this.marcaDAO.save(marca);
			}
			throw new ApiNotFoundException(ApiMessageSource.toMessageSetObject("objeto.add.error", "nome"));
		} catch (NoSuchIndexException e) {
			marca.setId(elastic.identifierId(Marca.class));
			marca.setTenantID(index.getTenantID());
			return this.marcaDAO.save(marca);
		}
	}
	/**
	 * Buscar todas as Marcas
	 * @return
	 */
	public Iterator<Marca> buscarAll(){
		return marcaDAO.findAll().iterator();
	}
	/**
	 * Buscar todas as Categorias - Paginacao
	 * 
	 * @param pageable
	 * @return
	 */
	public Page<Marca> buscarAllPagination(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("nome"));
		Page<Marca> pageResult = marcaDAO.findAll(pageRequest);
		return new PageImpl<>(pageResult.toList(), pageRequest, pageResult.getTotalElements());
	}
	/**
	 * Buscar todas as marcas referente ao ID Informado
	 * @param ids
	 * @return
	 */
	public Iterator<Marca> buscarAllId(String[] ids) {
		return marcaDAO.findAllById(Arrays.asList(ids)).iterator();
	}
	
	/**
	 * Deletar Marca assando o id como parametro
	 * @param idMarca
	 */
	public void excluir(String idMarca) {
		marcaDAO.deleteById(idMarca);
	}
	/**
	 * Buscar uma Marca passando o Nome como parametro
	 * @param nome
	 * @return
	 * @throws ApiNotFoundException
	 */
	public Marca buscarPorNome(String nome) throws ApiNotFoundException {
		return  marcaDAO.findByNome(nome).orElseThrow(() -> (new ApiNotFoundException(ApiMessageSource.toMessageSetObject("objeto.error.null.msg.object", "Marca"))));
	}
	/**
	 * Buscar uma Marca passando o ID como parametro
	 * @param id
	 * @return
	 * @throws ApiNotFoundException
	 */
	public Marca buscarPorId(String id) throws ApiNotFoundException {
		return marcaDAO.findById(id).orElseThrow(() -> (new ApiNotFoundException(ApiMessageSource.toMessageSetObject("objeto.error.null.msg.object", "Marca"))));	
	}
}
