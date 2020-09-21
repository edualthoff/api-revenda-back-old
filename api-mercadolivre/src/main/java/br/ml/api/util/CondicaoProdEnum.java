package br.ml.api.util;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum CondicaoProdEnum {

	USADO("usado"), NOVO("novo"), RECONDICIONADO("recondicionado"),;

	private String status;

	private CondicaoProdEnum(String status) {
		this.status = status;
	}

	public String getCondicao() {
		return status;
	}

	@JsonCreator
	public static String setCondicao(String cond) {
		switch (cond.toLowerCase()) {
		case "usado": {
			return CondicaoProdEnum.USADO.getCondicao();
		}
		case "novo": {
			return CondicaoProdEnum.NOVO.getCondicao();
		}
		case "recondicionado": {
			return CondicaoProdEnum.RECONDICIONADO.getCondicao();
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + cond);
		}
	}
}
