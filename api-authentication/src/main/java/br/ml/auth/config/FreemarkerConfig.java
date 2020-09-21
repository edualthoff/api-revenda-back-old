package br.ml.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FreemarkerConfig {

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates"); // defines the classpath location of the
																			// freemarker templates
		freeMarkerConfigurer.setDefaultEncoding("UTF-8"); // Default encoding of the template files
		return freeMarkerConfigurer;
	}


}
