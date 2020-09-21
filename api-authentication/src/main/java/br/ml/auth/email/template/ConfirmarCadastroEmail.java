package br.ml.auth.email.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import br.ml.auth.config.ApplicationContextProvider;
import br.ml.auth.email.Mail;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.Data;

public class ConfirmarCadastroEmail implements TemplateEmailConfig {
	private static final Logger log = LoggerFactory.getLogger(ConfirmarCadastroEmail.class);

	private JavaMailSender emailSender;

	private FreeMarkerConfigurer freemarkerConfig;
	private Mail mail;
	private TemplatePropriety model;

	private String URL_LOGO = "https://edualthoff.000webhostapp.com/revenda-logo.png";
	public ConfirmarCadastroEmail(Mail mail, String usuario, String link) {
		super();
		this.model = new TemplatePropriety(usuario, link);
		this.mail = mail;
	}

	public MimeMessage template() {
		this.freemarkerConfig = ApplicationContextProvider.getApplicationContext()
				.getBean(FreeMarkerConfigurer.class);
		this.emailSender = ApplicationContextProvider.getApplicationContext()
				.getBean(JavaMailSender.class);
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			
			//helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("usuario", model.getUsuario());
			data.put("linkConfirmar", model.getLinkConfirmar());
			data.put("image", URL_LOGO);
			
			Template t = freemarkerConfig.getConfiguration().getTemplate("index.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, data);
			
			helper.setTo(mail.getTo());
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			//helper.setFrom(mail.getFrom());

		} catch (MessagingException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (TemplateNotFoundException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (TemplateException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return message;
	}

	@Data
	protected class TemplatePropriety {
		String usuario;
		String linkConfirmar;

		public TemplatePropriety(String usuario, String linkConfirmar) {
			super();
			this.usuario = usuario;
			this.linkConfirmar = linkConfirmar;
		}

	}
}
