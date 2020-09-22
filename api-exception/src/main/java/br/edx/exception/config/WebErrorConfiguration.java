package br.edx.exception.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(value = -1)
public class WebErrorConfiguration {
	
	@Value("${api.version}")
	private String currentApiVersion;
	@Value("${api.sendreport.uri}")
	private String sendReportUri;

	/**
	 * We override the default {@link DefaultErrorAttributes}
	 *
	 * @return A custom implementation of ErrorAttributes
	 */
	@Bean
	public ErrorAttributes errorAttributes() {
		return new ApiErrorAttributes(currentApiVersion, sendReportUri);
	}
}
