package br.ml.auth.usuario;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import br.ml.auth.email.EmailService;
import freemarker.template.TemplateException;

@RestController
@RequestMapping(path = "/oauth/user")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired EmailService emailService;
	
	/**
	 * Cadastrar um novo usuario
	 * @param user
	 * @param codigoGerado
	 * @return
	 */
	@PostMapping()
	public Usuario createUsuarioCodigo(@RequestBody @Valid Usuario user, 
			@RequestParam(name = "codigo_cadastro", required = true) String codigoGerado) throws MessagingException, IOException, TemplateException {
		return this.usuarioService.createUsuarioComCodigoCadstro(user, codigoGerado);
	}
	/**
	 * Verificar Cadastro
	 * @param codigoVerificar
	 */
	@PostMapping("/verificar/{codigoVerificar}")
	public void verificarEmail(@PathVariable(value = "codigoVerificar") String codigoVerificar) {
		System.out.println("path "+codigoVerificar);
		this.usuarioService.verificarEmail(codigoVerificar);
	}
	/**
	 * Enviar Email para confirmar cadastro
	 * @param codigoVerificar
	 */
	@PostMapping("/enviar-email/{email}")
	public void reenviarVerificarEmail(@PathVariable(value = "email") String codigoVerificar) throws MessagingException, IOException, TemplateException {
		this.usuarioService.reenviarEmailConfirmacao(codigoVerificar);
	}
	/**
	 * Email para recuperar senha
	 * @param email
	 */
	@PostMapping("/senha/{email}")
	public void verificarEmailSenha(@PathVariable(value = "email") String email) throws IOException, MessagingException, TemplateException {
		System.out.println("path "+email);
		this.usuarioService.enviarEmailParaRecuperarSenha(email);
	}
	/**
	 * Atualizar Senha
	 * @param password
	 */
	@PostMapping("/senha")
	public void recuperarSenha(@RequestParam(value = "password", required = true) String password,
		@RequestParam(value = "codigo", required = true) String codigo)
			throws MessagingException, IOException, TemplateException {
		this.usuarioService.atualizarSenha(password, codigo);
	}	
	
	@PostMapping("/teste")
	public String createUsuarioCodigo2() throws MessagingException, IOException, TemplateException {
		String b = UriUtils.encode("CWvWtmwo034yfW8hdayIdSIOaOiGyxFN-xa7bDfqOnvvvMyBuV76OBu7cwClxvHSnWX2hAnHugCg-X09NhyLhQ==", "UTF-8");
		String a = UriUtils.decode(b, "UTF-8");
		System.out.println("a "+ b);
		System.out.println("\n"+a);
		
		return "send";
	}

}
