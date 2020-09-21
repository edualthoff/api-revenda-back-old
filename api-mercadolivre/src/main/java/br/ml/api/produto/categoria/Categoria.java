package br.ml.api.produto.categoria;


import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import br.ml.api.config.elk.base.BaseImplements;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
@TypeAlias("categorias")
@Document(indexName = "categorias#{@indexTenantDynamic.getIndex()}", shards = 1, replicas = 1, createIndex = true )
public class Categoria extends BaseImplements {
	private static final long serialVersionUID = 8336967648507401231L;
	
	@NotBlank(message = "{name.not.blank}")
	@Field(type = FieldType.Text, index = true, fielddata = true )
	@Setter(value = AccessLevel.NONE)
	private String nome;
	@Field(type = FieldType.Boolean)
	private boolean status;
	
	public Categoria() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param id
	 * @param tenantID
	 * @param nome
	 * @param status
	 */
	public Categoria(String id, String tenantID, @NotBlank(message = "{name.not.blank}") String nome, boolean status) {
		super(id, tenantID);
		this.nome = nome;
		this.status = status;
	}
	
	public void setNome(String nome) {
		this.nome = nome.strip().toLowerCase();
	}

}
