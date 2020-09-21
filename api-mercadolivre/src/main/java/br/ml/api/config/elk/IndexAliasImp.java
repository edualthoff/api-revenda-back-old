package br.ml.api.config.elk;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IndexAliasImp implements IndexAlias {

	@Autowired
	private ElasticsearchOperations elkTemplate;

	@Autowired
	private IndexTenantDynamic indexTenant;

	@Override
	public <T> void createIndexAlias(Class<T> clazz) {
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

	}
}
