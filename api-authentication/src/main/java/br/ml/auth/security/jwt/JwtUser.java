package br.ml.auth.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUser implements UserDetails {
	private static final long serialVersionUID = 368980034232792015L;

	private Long id;
	private String username;
	private String password;
	private boolean verificado;
	private String session;
	private boolean userAtivo;
	private Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	private List<String> moduloAplicacao;
	
	/*public JwtUser(Long id, boolean verificado, String username, String session,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.verificado = verificado;
		this.session = session;
		this.authorities = authorities;
	}*/
	
	public JwtUser(Long id, String username, String password, boolean verificado , boolean userAtivo, 
			String session, List<String> moduloAplicacao, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.verificado = verificado;
		this.authorities = authorities;
		this.userAtivo = userAtivo;
		this.session = session;
		this.moduloAplicacao = moduloAplicacao;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isEnabled() {
		return userAtivo;
	}
	
	@JsonIgnore
	public boolean getVerificado() {
		return verificado;
	}
	@JsonIgnore
	public String getSession() {
		return session;
	}
	@JsonIgnore
	public List<String> getModuloAplicacao() {
		return moduloAplicacao;
	}	
}
