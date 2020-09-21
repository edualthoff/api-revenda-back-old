package br.ml.api.crawler;

import java.util.List;


public interface BuscaCrawler<T> {

	List<T> analisarUrl(String url);
	
	 
}
