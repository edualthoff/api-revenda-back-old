package br.ml.api.buscas;

public interface ValidarCampos<T> {

	void linkUrl();
	
	void rangeValue();
	
	T build();
}
