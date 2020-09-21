package br.ml.gateway.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter {

	@Bean
	CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Arrays.asList("*", "http://localhost"));
		corsConfig.setMaxAge(8000L);
		corsConfig.addAllowedMethod("POST, GET, OPTIONS, DELETE, PUT");
		corsConfig.addAllowedHeader("x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN, X-TenantId, tenantID");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}
}
