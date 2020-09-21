package br.ml.api.crawler;

public interface FormatarLinks<T> {

	String gerarLinks(T url);
	
}
