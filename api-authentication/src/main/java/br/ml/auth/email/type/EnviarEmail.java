package br.ml.auth.email.type;

import java.io.IOException;

import javax.mail.MessagingException;

import freemarker.template.TemplateException;

public interface EnviarEmail<T> {

	void sendEmail() throws MessagingException, IOException, TemplateException;
	
	void setObjectoClzz(T clazz) throws IOException;
	
}
