package br.ml.api.config.elk;

public interface ElasticsearchIdentifierGenerator {

	<T> String identifierId(Class<T> clazz);
}
