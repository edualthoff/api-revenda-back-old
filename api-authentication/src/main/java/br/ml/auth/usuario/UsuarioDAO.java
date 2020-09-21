package br.ml.auth.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
	
	//@Nullable
	Usuario findByEmail(String email);
	boolean existsByEmail(String email);

}
