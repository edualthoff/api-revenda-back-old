package br.ml.api.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;
    
    public void sendMail(String destinatario, String assunto, String mensagem) {
    	log.debug("MailServicer - Netodo sendMail -  Start");
    	try {
    		MimeMessage mail = this.mailSender.createMimeMessage();
    		MimeMessageHelper mountEmail = new MimeMessageHelper(mail);
    		
			mountEmail.setTo(destinatario);
			mountEmail.setSubject(assunto);
			mountEmail.setText(mensagem);

			this.mailSender.send(mail);
			
    	} catch (MessagingException e) {
    		log.error("Falha ao enviar o email, {}, {}", destinatario, assunto);
			e.printStackTrace();
		}
    }
}
