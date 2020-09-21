package br.ml.auth.email.template;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import freemarker.template.TemplateException;

public interface TemplateEmailConfig {

	MimeMessage template() throws MessagingException, IOException, TemplateException;
}
