package br.ml.auth.cadastro.codigo;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.ml.auth.modulo.ModuloAplicacao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@EqualsAndHashCode
@Entity
@Table(name = "tb_codigo_cadastro")
@EntityListeners(AuditingEntityListener.class)
public class CodigoCadastro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo_id")
	private Long id;
	
	@Column(name = "codigo_gerado")
	private String codigoGerado;
	
	@Column(name = "condicao")
	private boolean condicao;
	
	@Column(name = "usuario_id")
	private Long usuarioId;
	
	@Column(name = "data_created", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date dataCreated;
	
	@Column(name = "data_expired", updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataExpired;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "tb_codigo_cadastro_tb_modulo",
    joinColumns = {
            @JoinColumn(name = "codigo_id_tb_codigo_cadastro", referencedColumnName = "codigo_id", updatable = false)},
    inverseJoinColumns = {
            @JoinColumn(name = "modulo_id_tb_modulo", referencedColumnName = "modulo_id", updatable = false)})
	private List<ModuloAplicacao> moduloAplicacao;
    
}
