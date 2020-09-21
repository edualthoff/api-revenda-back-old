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
import br.ml.auth.email.template.RecuperarSenhaEmail;
import br.ml.auth.email.template.TemplateEmailConfig;
import br.ml.auth.usuario.Usuario;
import br.ml.auth.usuario.codigo.CodigoAtivar;
import br.ml.auth.usuario.codigo.CodigoRecuperarSenha;
import freemarker.template.TemplateException;

@Service()
@Qualifier("emailRecuperarSenha")
public class EmailRecuperarSenha implements EnviarEmail<Usuario>{

	@Autowired private Environment environment;
	@Autowired private EmailService emailService;
	private Usuario user;
	
	/**
	 * Enviar de Confirmacao de cadastro
	 * @param user
	 * @throws MessagingException
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Override
	public void sendEmail() throws MessagingException, IOException, TemplateException {
		CodigoAtivar<Usuario> codigo = new CodigoRecuperarSenha(user);
    	Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject("Email para recuperar senha, Revenda Certa");
        TemplateEmailConfig cc = new RecuperarSenhaEmail(mail, user.getPessoa().getNome(),
        		this.environment.getProperty("front.server.url-auth-recuperar-senha")
        		+ UriUtils.encode(codigo.gerarCodigo(), "UTF-8"));
		emailService.sendSimpleMessage(cc.template());
		
	}

	@Override
	public void setObjectoClzz(Usuario clazz) throws IOException {
			this.user = clazz;	
	}
}