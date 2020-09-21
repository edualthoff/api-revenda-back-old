package br.ml.api.buscas.mercadolivre.itens;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.validation.constraints.Digits;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"date_modified"})
@TypeAlias("buscas/itens/mercadolivre")
@Document(indexName = "buscas-itens-mercadolivre", shards = 2, replicas = 1, createIndex = true)
//@Document(indexName = "buscas-itens-mercadolivre", shards = 1, replicas = 1, createIndex = true)
public class ItemMercadoLivre implements Persistable<String>, Serializable{
	private static final long serialVersionUID = 6677346919685856254L;

	@Id
	//@Field(type = FieldType.Keyword)
	private String id;
	@Field(type = FieldType.Text)
	private String titulo;
	@Field(type = FieldType.Text)
	private String condicao;
	@Field(type = FieldType.Double)
	@Digits(integer=10, fraction=2)
	private double valor;
	@Field(type = FieldType.Text)
	private String descricao;
	@Field(type = FieldType.Text)
	private String cidade;
	@Field(type = FieldType.Text, index = false, store = false )
	private String linkUrl;
	@Field(type = FieldType.Text)
	private String tipoAnuncio;
	@Field(type = FieldType.Boolean, index = true)
	private boolean ativo;
	@Field(type = FieldType.Keyword)
	private List<String> idUriBuscas;
	@Field(type = FieldType.Keyword)
	private List<String> tenantID;
	@Field(type=FieldType.Date, format=DateFormat.basic_date_time_no_millis)
	@JsonFormat(pattern = "yyyyMMdd'T'HHmmssZ", timezone = "UTC")
	@CreatedDate
	private Instant date_created;
	@Field(type=FieldType.Date, format=DateFormat.basic_date_time_no_millis)
	@LastModifiedDate
	@JsonFormat(pattern = "yyyyMMdd'T'HHmmssZ", timezone = "UTC")
	private Instant date_modified;
	

	public ItemMercadoLivre(String id, String titulo, String condicao, @Digits(integer = 10, fraction = 2) double valor,
			String descricao, String cidade, String linkUrl, String tipoAnuncio, boolean ativo,
			List<String> idUriBuscas, List<String> tenantID) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.condicao = condicao;
		this.valor = valor;
		this.descricao = descricao;
		this.cidade = cidade;
		this.linkUrl = linkUrl;
		this.tipoAnuncio = tipoAnuncio;
		this.ativo = ativo;
		this.idUriBuscas = idUriBuscas;
		this.tenantID = tenantID;
	}
	
	public ItemMercadoLivre() {
		super();
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return id == null || (date_created == null);	
	}

	@Override
	public String getId() {
		return id;
	}
}
