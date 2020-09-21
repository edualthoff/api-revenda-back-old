package br.ml.api.base.mercadolivre;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BuscarProdutoScheduling {
	private static final Logger log = LoggerFactory.getLogger(BuscarProdutoScheduling.class);

	@Autowired
	private VerificarSiteMercadoLivre verificarSiteMercadoLivre;

	
	//@Scheduled(cron = "0 */20 9-23 * * *", zone = "America/Sao_Paulo")
	//@Scheduled(cron = "0 */2 * * * *", zone = "America/Sao_Paulo")
	public void verificarCada20min() {
		log.debug("Start @Scheduled das 9 ate 23 horas e cada 20 min- start: {}, Thread nome: {}", LocalDateTime.now(), Thread.currentThread().getName());
		verificarSiteMercadoLivre.baseAnaliseThread();
		// Thread.currentThread().interrupt();
	}
	
	@Scheduled(cron = "0 0 0-9 * * *", zone = "America/Sao_Paulo")
	//@Scheduled(fixedRate = 7200 )
	public void verificarAposMeiaNoite() {
		log.debug("Start @Scheduled Apos 23 horas - start: {}, Thread nome: {}", LocalDateTime.now(), Thread.currentThread().getName());
		verificarSiteMercadoLivre.baseAnaliseThread();
		// Thread.currentThread().interrupt();
	}
}
