package br.ml.auth.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ml.auth.usuario.Usuario;
import br.ml.auth.usuario.UsuarioDAO;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioDAO userDAO;

	@SuppressWarnings("unused")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = userDAO.findByEmail(username);
		//System.out.println("loadUserByUsername2 " + user.getEmail());

		if (user == null) {
			throw new UsernameNotFoundException("Not found username: " + username);
		}

		return JwtUserFactory.create(user);
	}
}
