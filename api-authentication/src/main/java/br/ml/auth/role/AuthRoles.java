package br.ml.auth.role;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.ml.auth.usuario.Usuario;

@Entity
@Table(name = "tb_auth_roles")
@IdClass(AuthRoles.AuthRolesPK.class)
public class AuthRoles implements Serializable{
	private static final long serialVersionUID = -1646806133663651594L;

	static class AuthRolesPK implements Serializable {
		private static final long serialVersionUID = 8540097502525157728L;
		private RolesProfileEnum roles;
		private Long usuarioId;

		public AuthRolesPK() {}

		public AuthRolesPK(RolesProfileEnum roles, Long usuarioId) {
			this.roles = roles;
			this.usuarioId = usuarioId;
		}
		public RolesProfileEnum getRoles() {
			return roles;
		}
		public void setRoles(RolesProfileEnum roles) {
			this.roles = roles;
		}

		public Long getUsuarioId() {
			return usuarioId;
		}

		public void setUsuarioId(Long usuarioId) {
			this.usuarioId = usuarioId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((roles == null) ? 0 : roles.hashCode());
			result = prime * result + ((usuarioId == null) ? 0 : usuarioId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AuthRolesPK other = (AuthRolesPK) obj;
			if (roles != other.roles)
				return false;
			if (usuarioId == null) {
				if (other.usuarioId != null)
					return false;
			} else if (!usuarioId.equals(other.usuarioId))
				return false;
			return true;
		}			
	}
	
	@Id
	@Column(name="auth_roles", nullable = false)
	@Enumerated(EnumType.STRING)
	private RolesProfileEnum roles;
	
	@Id
	@Column(name = "usuario_id_tb_usuario", nullable = false)
	private Long usuarioId;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id_tb_usuario", insertable = false, updatable = false, referencedColumnName = "usuario_id")
	private Usuario usuario;

	public AuthRoles() {
	}
	
	public AuthRoles(RolesProfileEnum roles) {
		super();
		this.roles = roles;
	}

	
	public AuthRoles(RolesProfileEnum roles, Usuario usuario) {
		super();
		this.roles = roles;
		this.usuarioId = usuario.getId();
		this.usuario = usuario;
	}

	public AuthRoles(RolesProfileEnum roles, Long usuarioId) {
		super();
		this.roles = roles;
		this.usuarioId = usuarioId;
	}

	public RolesProfileEnum getRoles() {
		return roles;
	}

	public void setRoles(RolesProfileEnum roles) {
		this.roles = roles;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((usuarioId == null) ? 0 : usuarioId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthRoles other = (AuthRoles) obj;
		if (roles != other.roles)
			return false;
		if (usuarioId == null) {
			if (other.usuarioId != null)
				return false;
		} else if (!usuarioId.equals(other.usuarioId))
			return false;
		return true;
	}
}
