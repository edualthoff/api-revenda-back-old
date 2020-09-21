package br.edx.exception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.WebSecurityEnablerConfiguration;

import br.edx.exception.config.WebErrorConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude= {SecurityAutoConfiguration.class, WebErrorConfiguration.class, WebSecurityEnablerConfiguration.class, EnableAutoConfiguration.class})
public class ApiExceptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiExceptionApplication.class, args);
	}

}
