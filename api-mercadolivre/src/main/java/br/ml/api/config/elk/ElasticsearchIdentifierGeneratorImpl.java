package br.ml.api.config.elk;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ElasticsearchIdentifierGeneratorImpl implements ElasticsearchIdentifierGenerator {
	private static final Logger log = LoggerFactory.getLogger(ElasticsearchIdentifierGeneratorImpl.class);

	@Autowired
	private ElasticsearchOperations elkTemplate;
	@Autowired
	private IndexAlias indexAlias;
	
	private String PREFIX_DB = "EDB";
	private String COUNT_START = "100";

	public synchronized <T> String identifierId(Class<T> clazz) {
		log.debug("Entrou na metodo identifierId");
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withSort(SortBuilders.fieldSort("date_created").order(SortOrder.DESC)).build();

		try {
			log.debug("index de consultar ID: {}", elkTemplate.getIndexCoordinatesFor(clazz).getIndexName());

			if (!elkTemplate.indexOps(clazz).exists()) {
				log.debug("Index Alias não existe, index: {}, alias: {}",
						elkTemplate.getIndexCoordinatesFor(clazz).getIndexName()
								.split(IndexTenantDynamic.PREFIX_SIMBOL)[0],
						elkTemplate.getIndexCoordinatesFor(clazz).getIndexName());
				this.indexAlias.createIndexAlias(clazz);
				return (PREFIX_DB + COUNT_START);
			}
			String idBaseReturn = elkTemplate.searchOne(searchQuery, clazz, IndexCoordinates.of(elkTemplate
					.getIndexCoordinatesFor(clazz).getIndexName().split(IndexTenantDynamic.PREFIX_SIMBOL)[0])).getId()
					.split(PREFIX_DB)[1];

			return (PREFIX_DB + (Long.valueOf(idBaseReturn) + 1L));

		} catch (IndexOutOfBoundsException e) {
			log.error("Error ao Gerar o ID " + e.getMessage());
		} catch (NullPointerException e) {
		}
		return (PREFIX_DB + COUNT_START);
	}

	/** Descontinuado - Separado Create class IndexAliasImp implements IndexAlias 
	 * 
	private <T> void createIndexAlias(Class<T> clazz) {
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.termQuery(IndexTenantDynamic.TENANT_VAR, indexTenant.getTenantID())).build();

		AliasActionParameters act = AliasActionParameters.builder()
				.withIndices(elkTemplate.getIndexCoordinatesFor(clazz).getIndexName()
						.split(IndexTenantDynamic.PREFIX_SIMBOL)[0])
				.withAliases(elkTemplate.getIndexCoordinatesFor(clazz).getIndexName()).withFilterQuery(searchQuery)
				.build();

		elkTemplate.indexOps(IndexCoordinates.of(
				elkTemplate.getIndexCoordinatesFor(clazz).getIndexName().split(IndexTenantDynamic.PREFIX_SIMBOL)[0]))
				.alias(new AliasActions().add(new AliasAction.Add(act)));
	}*/
}
