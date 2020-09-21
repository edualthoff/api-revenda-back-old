package br.ml.api.config.elk.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @class Abstract
 * 
 * Base para inserir, alterar, remover um objeto no ELASTICKSARCH
 * - Para Multi Tenant
 * - Contem @parm ID, @parm date_created, @parm date_modified, @parm tenantID
 * 
 * @author edu
 *
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(exclude = {"date_modified"})
public abstract class BaseImplements implements Persistable<String>, Serializable{
	private static final long serialVersionUID = -6385624386006107246L;

	@Id
	private String id;	
	@Field(type=FieldType.Date, format=DateFormat.date_hour_minute_second)
	@CreatedDate
	private LocalDateTime date_created;
	@Field(type=FieldType.Date, format=DateFormat.date_hour_minute_second)
	@LastModifiedDate
	private LocalDateTime date_modified;
	@Field(type = FieldType.Text)
	//@JsonIgnore
	private String tenantID;
	
	public BaseImplements() {
		super();
	}

	public BaseImplements(String id, String tenantID) {
		super();
		this.id = id;
		this.tenantID = tenantID;
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
