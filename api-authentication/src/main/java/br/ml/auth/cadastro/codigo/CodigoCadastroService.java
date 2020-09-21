package br.ml.auth.cadastro.codigo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodigoCadastroService {

	@Autowired private CodigoCadastroDAO codigoCadastroDAO;
	
	/**
	 * Retorna se o codigo de cadastro esta ativo ou inativo - true ou false
	 * @param codigo_gerado
	 * @return
	 */
	public boolean validCodigoCadastro(String codigo_gerado) {
		return codigoCadastroDAO.existsByCodigoGerado(codigo_gerado);
	}
	
	/**
	 * Retorna uma instancia de CodigoCadastro, buscando pelo codigo que foi gerado
	 * @param codigoGerado
	 * @return
	 */
	public CodigoCadastro buscarCodigo(String codigoGerado) {
		return codigoCadastroDAO.findByCodigoGerado(codigoGerado);
	}
}
