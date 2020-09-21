package br.ml.api.produto.item;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemProdutoDAO extends ElasticsearchRepository<ItemProduto, String> {

	Iterable<ItemProduto> findAllByIdCategoria(Iterable<String> idCategoria);
	
	Iterable<ItemProduto> findAllByIdMarca(Iterable<String> idMarca);
	
	Optional<ItemProduto> findByModelo(String modelo);
	
	Page<ItemProduto> findByModelo(String modelo, Pageable pageable);
	
}
