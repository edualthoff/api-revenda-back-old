package br.ml.auth.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.ml.auth.modulo.ModuloAplicacao;
import br.ml.auth.role.AuthRoles;
import br.ml.auth.session.SessionService;
import br.ml.auth.session.SessionServiceImp;
import br.ml.auth.usuario.Usuario;


/* 
 * Classe responsavel por pegar as credencias de acesso do usuario que é necessario para validar no token e
 * realizar o redirecionamento no sistema.
 * 
 * Classe responsavel por implementar a classes "class JwtUser implements UserDetail", passando as info necessarias.
 * */

public class JwtUserFactory {
	private static final Logger log = LogManager.getLogger(JwtUserFactory.class);

	private JwtUserFactory() {}

	public static JwtUser create(Usuario user) {
		log.debug("Id Usuario / Person {}", user.getId());

		return new JwtUser(
				user.getId(), 
				user.getEmail(), 
				user.getSenha(), 
				user.isVerificado(),
				user.isUserAtivo(),
				sessionCreate(new SessionServiceImp(user.getTenantId(), user.getId())),
				moduloAplicacao(user.getModuloAplicacao()),
				mapToGrantedAuthorities(user.getAuthRole()));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<AuthRoles> authRoles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (int i = 0; i < authRoles.size(); i++) {
			AuthRoles role = authRoles.get(i);
			authorities.add(new SimpleGrantedAuthority(role.getRoles().toString()));
		}
		return authorities;
	}
	
	private static String sessionCreate(SessionService sessionService) {
		return sessionService.gerarSession();
	}
	private static List<String> moduloAplicacao(List<ModuloAplicacao> listModulo) {
		List<String> modulo = new ArrayList<>();
		listModulo.forEach((ModuloAplicacao x) -> {modulo.add(x.getNome());});
		return modulo; 
	}
}
