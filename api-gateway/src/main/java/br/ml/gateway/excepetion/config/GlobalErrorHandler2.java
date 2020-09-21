package br.ml.gateway.excepetion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.ml.gateway.excepetion.msg.ApiErrorCodeException;
import br.ml.gateway.excepetion.msg.ApiErrorMessage;
import br.ml.gateway.excepetion.type.ApiBadRequestException;
import reactor.core.publisher.Mono;



public class GlobalErrorHandler2 implements ErrorWebExceptionHandler {

	@Value("${api.version}")
	private String currentApiVersion;
	@Value("${api.sendreport.uri}")
	private String sendReportUri;

	@Autowired 
	private ErrorAttributes defaultErrorAttributes;
	
	@Override
	public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).findAndRegisterModules();
		DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
		DataBuffer dataBuffer = null;
	
		if (throwable instanceof RuntimeException) {
			ApiErrorCodeException api = (ApiBadRequestException) throwable;
			serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			ApiErrorMessage error = new ApiErrorMessage(currentApiVersion,
					api.getErrorCode(),
					api.getMessageError(),
					serverWebExchange.getRequest().getPath().value(),
					throwable.getMessage(),
					ApiMessageSource.toMessage("msg.padrao"), 
					sendReportUri);
			
			try {
	
				dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(error));
			} catch (JsonProcessingException e) {
				dataBuffer = bufferFactory.wrap("".getBytes());
			}
			serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
			return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
		}
		serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		serverWebExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		try {
			defaultErrorAttributes.storeErrorInformation(throwable, serverWebExchange);
			serverWebExchange.getAttributes();
			dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(defaultErrorAttributes));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverWebExchange.getResponse().setComplete();
	}

}
