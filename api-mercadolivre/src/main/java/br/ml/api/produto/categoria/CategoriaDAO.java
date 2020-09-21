package br.ml.api.produto.categoria;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaDAO extends ElasticsearchRepository<Categoria, String>{

	Optional<Categoria> findByNome(String nome);


}
