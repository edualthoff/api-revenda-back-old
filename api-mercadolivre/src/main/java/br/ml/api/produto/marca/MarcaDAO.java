package br.ml.api.produto.marca;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaDAO extends ElasticsearchRepository<Marca, String>{

	Optional<Marca> findByNome(String nome);
	Optional<Marca> findById(String idMarca);
}
