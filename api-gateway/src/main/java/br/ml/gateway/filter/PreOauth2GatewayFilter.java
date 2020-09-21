package br.ml.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import br.ml.gateway.excepetion.config.ApiMessageSource;
import br.ml.gateway.excepetion.type.ApiBadRequestException;
import reactor.core.publisher.Mono;

@Component()
public class PreOauth2GatewayFilter implements GlobalFilter {
	private static final Logger log = LoggerFactory.getLogger(PreOauth2GatewayFilter.class);

	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// grab configuration from Config object
		log.debug("Filter Gateway PreOauth2GatewayFilter");

		if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
			if (exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION).contains("Bearer")) {
				return chain.filter(exchange);
			} else if (exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION).contains("Basic")) {
				return chain.filter(exchange);
			}
		}
		return Mono.error(new ApiBadRequestException(ApiMessageSource.toMessage("bad_request.error.code.headers"), ApiMessageSource.toMessage("bad_request.error.msg.headers")));
		//throw new ApiBadRequestException(ApiMessageSource.toMessage("bad_request.error.code.headers"), ApiMessageSource.toMessage("bad_request.error.msg.headers"));

	}

}
