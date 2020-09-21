package br.ml.auth.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthRolesService {

	@Autowired private AuthRolesDAO authRolesDAO;
	
	public AuthRoles createRole(AuthRoles authRoles) {
		return this.authRolesDAO.save(authRoles);
	}
}
