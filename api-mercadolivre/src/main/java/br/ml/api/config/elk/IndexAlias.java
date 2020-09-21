package br.ml.api.config.elk;

public interface IndexAlias {

	<T> void createIndexAlias(Class<T> clazz);
	
}
