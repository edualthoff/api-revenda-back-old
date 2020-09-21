package br.ml.gateway.excepetion.config;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class ApiMessageSource {
	private static final Logger log = LoggerFactory.getLogger(ApiMessageSource.class);

	private static ResourceBundleMessageSource messageSource;
	
	@Autowired
	public ApiMessageSource(ResourceBundleMessageSource messageSource) {
		super();
		ApiMessageSource.messageSource = messageSource;
	}

	public MessageSource getMessageSource() {
		log.debug("MessageSource class staitc");
		return messageSource;
		
	}
	/*
	 * Seta a mensagem padrao informada passando a string correspondente
	 */
	public static String toMessage(String msg) {
		log.debug("MessageSource class metodod toMessage");
		return messageSource.getMessage(msg, null, Locale.ROOT);
	}
	/*
	 * Seta a mensagem padrao informada passando a string correspondente e informando o
	 * Objeto qual quer informar na mensagem
	 * 
	 */
	public static String toMessageSetObject(String msg, String name) {
		log.debug("MessageSource class metodod toMessage");
		return messageSource.getMessage(msg, new Object[] {name}, Locale.ROOT);
	}
	public static String toMessageSetDiversesObject(String msg, String name[]) {
		log.debug("MessageSource class metodod toMessage");
		return messageSource.getMessage(msg, new Object[] {name}, Locale.ROOT);
	}
}