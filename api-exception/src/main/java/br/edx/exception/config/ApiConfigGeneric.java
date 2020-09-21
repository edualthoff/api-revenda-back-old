package br.edx.exception.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class ApiConfigGeneric implements WebMvcConfigurer {

	
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("messages/ApiErrorMessages");
		// ms.setDefaultEncoding("UTF-8");
		ms.setUseCodeAsDefaultMessage(true);

		return ms;
	}
	
}
