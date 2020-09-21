package br.ml.auth.usuario;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.ml.auth.modulo.ModuloAplicacao;
import br.ml.auth.pessoa.Pessoa;
import br.ml.auth.role.AuthRoles;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Table(name = "tb_usuario")
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usuario_id")
	private Long id;
	
	@Column(name = "email")
	@NotBlank(message = "{email.not.blank}")
	@Email(message = "{email.not.valid}")
	private String email;

	@JsonIgnoreProperties({ "password" })
	@Column(name = "password")
	private String senha;
	
	@Column(name = "tenant_id")
	private String tenantId;
	
	@Column(name = "verificado")
	private boolean verificado;
	
	@Column(name = "user_ativo")
	private boolean userAtivo;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pessoa_id_tb_pessoa", referencedColumnName = "pessoa_id" )
	private Pessoa pessoa;
	
	@OneToMany(mappedBy = "usuario",
			orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private List<AuthRoles> authRole;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@Fetch(FetchMode.SUBSELECT) 
    @JoinTable(name = "tb_usuario_tb_modulo",
    joinColumns = {
            @JoinColumn(name = "usuario_id_tb_usuario", referencedColumnName = "usuario_id", updatable = false)},
    inverseJoinColumns = {
            @JoinColumn(name = "modulo_id_tb_modulo", referencedColumnName = "modulo_id", updatable = false)})
	private List<ModuloAplicacao> moduloAplicacao;
	
	@Column(name = "data_created", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date dataCreated;
	
	@Column(name = "data_update", updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date dataModified;
}
