package br.ml.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = {"br.ml.auth.*"})
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@ComponentScan({  "br.ml.auth.*", "br.edx.exception.*" })
public class ApiAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAuthenticationApplication.class, args);
	}

}
