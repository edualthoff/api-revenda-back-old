package br.ml.api.buscas.mercadolivre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ml.api.buscas.ValidarCampos;

public class ValidarCamposBuscas implements ValidarCampos<BuscasMercadoLivre>{
	private static final Logger log = LoggerFactory.getLogger(ValidarCamposBuscas.class);

	private BuscasMercadoLivre buscasML;
	
	
	public ValidarCamposBuscas(BuscasMercadoLivre buscasML) {
		this.buscasML = buscasML;
	}

	
	@Override
	public void linkUrl() {
		log.debug("url: {}",this.buscasML.getLinkUrl().strip().split("(\\_|\\&|\\#|\\?)")[0].replaceAll("(?i)(usado|novo|recondicionado)(\\/|)", "").replaceAll("!?[\\/]+$", "").trim()+"/");
		this.buscasML.setLinkUrl(this.buscasML.getLinkUrl().strip().split("(\\_|\\&|\\#|\\?)")[0].replaceAll("(?i)(usado|novo|recondicionado)(\\/|)", "").replaceAll("!?[\\/]+$", "").trim()+"/");
	}

	@Override
	public void rangeValue() {
		if((this.buscasML.getRangeInicial().compareTo(this.buscasML.getRangeFinal()) > 1 )) {
			this.buscasML.setRangeInicial(this.buscasML.getRangeFinal());
			this.buscasML.setRangeFinal(this.buscasML.getRangeInicial());
		}
	}

	@Override
	public BuscasMercadoLivre build() {
		this.linkUrl();
		this.rangeValue();
		return this.buscasML;
	}
}
