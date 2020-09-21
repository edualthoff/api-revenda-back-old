package br.ml.auth.email;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import freemarker.template.TemplateException;

@Service
public class EmailService {
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;
    

    @Async("sendEmailtaskExecutor")
    public void sendSimpleMessage(MimeMessage mail) throws MessagingException, IOException, TemplateException {
    	log.debug("Thread de envio de email");
   	
        emailSender.send(mail);
    }

}