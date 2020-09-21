package br.ml.api.produto.item;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import br.ml.api.config.elk.base.BaseImplements;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper=false)
@TypeAlias("produto/itens")
@Setting(settingPath = "data/es-config/elastic-analyzer.json")
//@Document(indexName = "itens#{@indexTenantDynamic.getIndex()}", shards = 2, replicas = 1, createIndex = true)
@Document(indexName = "itens#{@indexTenantDynamic.getIndex()}", shards = 1, replicas = 1, createIndex = true)
public class ItemProduto extends BaseImplements {
	private static final long serialVersionUID = -955659708585410073L;

	@Setter(value = AccessLevel.NONE)
	@Field(type = FieldType.Text, index = true, fielddata = true, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
	private String modelo;
	@Field(type = FieldType.Text)
	private String descricao;
	@Field(type = FieldType.Text)
	private String idCategoria;
	@Field(type = FieldType.Text)
	private String idMarca;
	

	public void setModelo(String modelo) {
		this.modelo = modelo.strip().toLowerCase();
	}


	/**
	 * @param id
	 * @param tenantID
	 * @param modelo
	 * @param descricao
	 * @param idCategoria
	 * @param idMarca
	 */
	public ItemProduto(String id, String tenantID, String modelo, String descricao, String idCategoria,
			String idMarca) {
		super(id, tenantID);
		this.modelo = modelo;
		this.descricao = descricao;
		this.idCategoria = idCategoria;
		this.idMarca = idMarca;
	}


	/**
	 * 
	 */
	public ItemProduto() {
		super();
	}	
	
}
