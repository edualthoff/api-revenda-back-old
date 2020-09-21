package br.ml.auth.tenant;

import java.time.Instant;

import org.springframework.data.repository.CrudRepository;

public class TenantService {

	//private Long numberStart = 100L;
	private Long numberStart =  Instant.now().toEpochMilli();
	private Long sizeSoma;
	
	
	private TenantService(Long sizeSoma) {
		super();
		this.sizeSoma = sizeSoma;
	}

	public String gerarTenantId() {
		String tentantId = String.valueOf(this.numberStart + this.sizeSoma);
		return tentantId;
	}
	
	/**
	 * Gerar Tenant ID do tipo String - Text - passando um reposito(CrudRepository) como parametro para pegar o size de Start 
	 * @param crudRepository - Type CrudRepository
	 * @return
	 */
	public static String buildIdString(CrudRepository<?, ?> crudRepository) {
		return new TenantService(crudRepository.count()).gerarTenantId();
	}
}
