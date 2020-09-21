package br.ml.api.base.mercadolivre;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.ml.api.buscas.mercadolivre.BuscasMercadoLivre;
import br.ml.api.buscas.mercadolivre.BuscasMercadoLivreService;
import br.ml.api.config.ApplicationContextProvider;
import br.ml.api.crawler.FormatarLinks;
import br.ml.api.crawler.mercadolivre.FormatarLinksMercadoLivre;

@Service
@Scope("prototype")
public class VerificarSiteMercadoLivre {
	private static final Logger log = LoggerFactory.getLogger(VerificarSiteMercadoLivre.class);

	@Autowired
	private BuscasMercadoLivreService buscasMLService;

	@Async("baseAnaliseThread")
	public void baseAnaliseThread() {
		log.debug("Thread - baseAnaliseThread - metodod: baseAnaliseThread START");
		int page = 0;
		Page<BuscasMercadoLivre> buscasMercadoLivre;
		List<BuscasMercadoLivre> buscasMercadoLivreAtualizadaNextDate = new ArrayList<>();
		do {
			buscasMercadoLivre = buscasMLService.buscarTodosParamStatusAndErrorLinkAndDataMenorOuIgual(true, false, Instant.now(), page);
			System.out.println("list size: "+buscasMercadoLivre.getSize());
			for (BuscasMercadoLivre buscaML : buscasMercadoLivre.getContent()) {
				FormatarLinks<BuscasMercadoLivre> gerarLink = new FormatarLinksMercadoLivre();
				String urlLink = gerarLink.gerarLinks(buscaML);
				log.debug("BuscasMercadoLivre while - Thread(baseCrawlerAnaliseThread) de link a percorrer: {} e idBusca: {}", urlLink, buscaML.getId());
				this.baseCrawlerAnaliseThread(buscaML, urlLink);
				buscaML.getSchedulingTime().setNextDate(buscaML.getSchedulingTime().nextDateUpdateOrCreate());
				buscasMercadoLivreAtualizadaNextDate.add(buscaML);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error("Thread baseCrawlerAnaliseThread - error executacao {}", e.getMessage());
				}
			}
			System.out.println("new date "+buscasMercadoLivreAtualizadaNextDate.get(0).getSchedulingTime().getNextDate());
			this.buscasMLService.atualizarTodos(buscasMercadoLivreAtualizadaNextDate);
			buscasMercadoLivreAtualizadaNextDate.clear();
			page++;
		} while (buscasMercadoLivre.hasNext());
	}

	@Async("baseCrawlerAnaliseThread")
	public void baseCrawlerAnaliseThread(BuscasMercadoLivre buscasMercadoLivre, String urlLink) {
		try {
			ApplicationContextProvider.getApplicationContext().getBean(BaseCrawlerAnalise.class).build(buscasMercadoLivre, urlLink);
		} catch (NullPointerException e) {
			log.error("Error baseCrawlerAnaliseThread - NullPointerException - {}", e);
			Thread.currentThread().interrupt();
		}
	}
}
