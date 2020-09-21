package br.ml.auth.cadastro.codigo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CodigoCadastroDAO extends CrudRepository<CodigoCadastro, Long> {

	boolean existsByCodigoGerado(String codigo_gerado);
	
	CodigoCadastro findByCodigoGerado(String codigoGerado);
	
}
