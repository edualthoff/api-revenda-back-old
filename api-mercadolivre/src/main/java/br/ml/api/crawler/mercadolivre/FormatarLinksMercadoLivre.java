package br.ml.api.crawler.mercadolivre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ml.api.buscas.mercadolivre.BuscasMercadoLivre;
import br.ml.api.crawler.FormatarLinks;

public class FormatarLinksMercadoLivre implements FormatarLinks<BuscasMercadoLivre> {
	private static final Logger log = LoggerFactory.getLogger(FormatarLinksMercadoLivre.class);

	@Override
	public String gerarLinks(BuscasMercadoLivre buscasMercadolivre) {
		log.debug("Link url BuscasMercadoLivre {}", buscasMercadolivre.getLinkUrl()
				+TagsRequestEnum.getTagsValue(buscasMercadolivre.getCondicao().getCondicao())
				+"/_DisplayType_LF"
				+TagsRequestEnum.getTagsValue("ordenar_price")
				+TagsRequestEnum.getTagsValue("price_range")+buscasMercadolivre.getRangeInicial()+"-"+buscasMercadolivre.getRangeFinal());
		
		return buscasMercadolivre.getLinkUrl()
				+TagsRequestEnum.getTagsValue(buscasMercadolivre.getCondicao().getCondicao())
				+"/_DisplayType_LF"
				+TagsRequestEnum.getTagsValue("ordenar_price")
				+TagsRequestEnum.getTagsValue("price_range")+buscasMercadolivre.getRangeInicial()+"-"+buscasMercadolivre.getRangeFinal();
	}
}
