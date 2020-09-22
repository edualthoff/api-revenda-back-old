package br.edx.exception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.edx.exception.*"})
@EnableAutoConfiguration()
public class ApiExceptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiExceptionApplication.class, args);
	}

}
