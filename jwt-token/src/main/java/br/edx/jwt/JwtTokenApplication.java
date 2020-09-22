package br.edx.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import br.edx.jwt.config.PropertySourcesConfigurer;

@PropertySource(factory = PropertySourcesConfigurer.class, value = "classpath:config.yaml")
@SpringBootApplication
public class JwtTokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtTokenApplication.class, args);
	}

}
