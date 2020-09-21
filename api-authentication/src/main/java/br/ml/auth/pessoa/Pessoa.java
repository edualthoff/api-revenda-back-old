package br.ml.auth.pessoa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_pessoa")
@EntityListeners(AuditingEntityListener.class)
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pessoa_id")
	private Long id;
	
	@Column(name = "nome")
	@NotBlank()
	@Setter(value = AccessLevel.NONE)
	private String nome;

	@Column(name = "sobrenome")
	@Setter(value = AccessLevel.NONE)
	private String sobrenome;
	
	@Column(name = "tenant_id")
	@JsonIgnoreProperties({ "tenant_id" })
	private String tenantId;
	
	@Column(name = "data_created", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date data_created;
	
	@Column(name = "data_update", updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date data_modified;
	

	public void setNome(String nome) {
		this.nome = nome.toLowerCase();
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome.toLowerCase();
	}

}
