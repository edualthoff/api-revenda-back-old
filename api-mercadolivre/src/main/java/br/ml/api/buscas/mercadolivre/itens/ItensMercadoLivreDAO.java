package br.ml.api.buscas.mercadolivre.itens;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItensMercadoLivreDAO extends ElasticsearchRepository<ItemMercadoLivre, String> {

	List<ItemMercadoLivre> findByIdUriBuscasAndAtivo(String idUriBuscas, boolean ativo);

	
}
