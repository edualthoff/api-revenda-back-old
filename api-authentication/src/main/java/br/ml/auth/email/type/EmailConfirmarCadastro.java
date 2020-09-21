package br.ml.auth.email.type;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import br.ml.auth.email.EmailService;
import br.ml.auth.email.Mail;
import br.ml.auth.email.template.ConfirmarCadastroEmail;
import br.ml.auth.usuario.Usuario;
import br.ml.auth.usuario.codigo.CodigoAtivarUsuario;
import freemarker.template.TemplateException;

@Service()
@Qualifier("emailConfirmarCadastro")
public class EmailConfirmarCadastro implements EnviarEmail<Usuario> {

	@Autowired
	private Environment environment;
	@Autowired
	private EmailService emailService;
	private Usuario user;

	/**
	 * Enviar de Confirmacao de cadastro
	 * 
	 * @param user
	 * @throws MessagingException
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Override
	public void sendEmail() throws MessagingException, IOException, TemplateException {
		Mail mail = new Mail();
		mail.setTo(user.getEmail());
		mail.setSubject("Email de confirmação do Revenda Certa");
		ConfirmarCadastroEmail cc = new ConfirmarCadastroEmail(mail, user.getPessoa().getNome(),
				this.environment.getProperty("front.server.url-auth-verificar")
						+ UriUtils.encode(new CodigoAtivarUsuario(user).gerarCodigo(), "UTF-8"));
		emailService.sendSimpleMessage(cc.template());

	}

	@Override
	public void setObjectoClzz(Usuario clazz) throws IOException {
		this.user = clazz;
	}
}
