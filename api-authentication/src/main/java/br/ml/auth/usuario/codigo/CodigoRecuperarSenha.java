package br.ml.auth.usuario.codigo;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.type.ApiBadRequestException;
import br.ml.auth.cifrador.Cifrador;
import br.ml.auth.usuario.Usuario;

public class CodigoRecuperarSenha implements CodigoAtivar<Usuario> {

	private Usuario user;
	private Long TEMPOS_PARA_ATIVAR_MINUTOS = 30L;
	private String separador = "/";

	public CodigoRecuperarSenha(Usuario user) {
		super();
		this.user = user;
	}

	public CodigoRecuperarSenha() {
		super();
	}

	@Override
	public String gerarCodigo() {
		Instant time = Instant.now();
		time.plus(Duration.ofMinutes(TEMPOS_PARA_ATIVAR_MINUTOS));
		String code = user.getEmail() + separador + user.getId().toString() + separador
				+ user.isVerificado() + separador + time;
		return Base64.getUrlEncoder().encodeToString(new Cifrador().criptografar(code));

	}

	@Override
	public Usuario reveterCodigo(String codigo) {
		String cifra = new Cifrador().descriptografar(Base64.getUrlDecoder().decode(codigo));

		if (Instant.now().compareTo(Instant.parse(cifra.split(separador)[3])) > 0) {
			user.setEmail(cifra.split(separador)[0]);
			user.setId(Long.parseLong(cifra.split(separador)[1]));
			user.setVerificado(Boolean.getBoolean(cifra.split(separador)[2]));
		} else {
			throw new ApiBadRequestException(ApiMessageSource.toMessage("bad_request.error.cadastroVerificar.code"),
					ApiMessageSource.toMessage("bad_request.error.cadastroVerificar.msg"));
		}
		return user;
	}

}
