package br.ml.auth.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.ml.auth.role.AuthRoles.AuthRolesPK;

@Repository
public interface AuthRolesDAO extends CrudRepository<AuthRoles, AuthRolesPK>{

}
