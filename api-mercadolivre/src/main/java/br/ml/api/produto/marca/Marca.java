package br.ml.api.produto.marca;

import javax.validation.constraints.NotBlank;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import br.ml.api.config.elk.base.BaseImplements;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(indexName = "marcas#{@indexTenantDynamic.getIndex()}", shards = 1, replicas = 1, createIndex = true )
public class Marca extends BaseImplements{
	private static final long serialVersionUID = 1834734113200288773L;

	@Field(type = FieldType.Text, index = true, fielddata = true)
	@NotBlank(message = "{name.not.blank}")
	@Setter(value = AccessLevel.NONE)
	private String nome;
	//@Field(type = FieldType.Text)
	//private String descricao;

	public Marca() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Marca(String id, String tenantID, String nome) {
		super(id, tenantID);
		this.nome = nome;
	}

	public void setNome(String nome) {
		this.nome = nome.strip().toLowerCase();
	}
}
