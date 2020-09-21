package br.ml.api.buscas.mercadolivre;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import br.ml.api.config.elk.base.BaseImplements;
import br.ml.api.util.CondicaoProdEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
//@Document(indexName = "busca-mercadolivre-produtos-#{@indexDynamic.getIndex()}", shards = 1, replicas = 1)
@Document(indexName = "buscas-produtos-mercadolivre#{@indexTenantDynamic.getIndex()}", shards = 1, replicas = 1, createIndex = true)
@TypeAlias("buscas/links/mercadolivre")
public class BuscasMercadoLivre extends BaseImplements implements Serializable{
	private static final long serialVersionUID = 8297426447697768554L;

	@Field(type = FieldType.Text, index = false )
	@NotEmpty
	private String linkUrl;
	@Field(type = FieldType.Text)
	private String descricao;
	@Field(type = FieldType.Text)
	private CondicaoProdEnum condicao;
	//@Field(type = FieldType.Scaled_Float, scalingFactor = 100)
	@Field(type = FieldType.Double)
	@Digits(integer=10, fraction=2)
	private BigDecimal rangeInicial;
	//@Field(type = FieldType.Scaled_Float, scalingFactor = 100)
	@Field(type = FieldType.Double)
	@Digits(integer=10, fraction=2)
	private BigDecimal rangeFinal;
	@Field(type = FieldType.Boolean, index = true)
	private boolean status;
	@Field(type = FieldType.Text)
	@NotEmpty
	private String idProduto;
	@Field(type = FieldType.Boolean, store = false)
	private boolean errorLink;
	@Field(type = FieldType.Nested)
	private SchedulingTime schedulingTime;
	
	public BuscasMercadoLivre() {
		super();
	}
	
	public BuscasMercadoLivre(@NotEmpty String linkUrl, CondicaoProdEnum condicao, boolean status, 
			@NotEmpty String idProduto) {
		super();
		this.linkUrl = linkUrl;
		this.condicao = condicao;
		this.status = status;
		this.idProduto = idProduto;
	}
}
