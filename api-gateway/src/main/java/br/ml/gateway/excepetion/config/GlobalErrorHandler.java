package br.ml.gateway.excepetion.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import br.ml.gateway.excepetion.msg.ApiErrorCodeException;
import br.ml.gateway.excepetion.msg.ApiErrorMessage;
import br.ml.gateway.excepetion.type.ApiBadRequestException;
import br.ml.gateway.excepetion.type.ApiExceptionCustom;
import reactor.core.publisher.Mono;


@Component
@Order(-2)
public class GlobalErrorHandler extends AbstractErrorWebExceptionHandler {

	@Value("${api.version}")
	private String currentApiVersion;
	@Value("${api.sendreport.uri}")
	private String sendReportUri;

	
    public GlobalErrorHandler(ApiErrorAttributes g, ApplicationContext applicationContext, 
            ServerCodecConfigurer serverCodecConfigurer) {
        super(g, new ResourceProperties(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        
		if (super.getError(request) instanceof ApiExceptionCustom) {
			Throwable throwable = getError(request);
			MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
					.from(throwable.getClass(), SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
			ApiErrorCodeException api = (ApiBadRequestException) throwable;
			ApiErrorMessage error = new ApiErrorMessage(currentApiVersion,
					api.getErrorCode(),
					api.getMessageError(),
					request.path(),
					throwable.getMessage(),
					ApiMessageSource.toMessage("msg.padrao"), 
					sendReportUri);
	        return ServerResponse.status(determineHttpStatus(throwable, responseStatusAnnotation))
	                .contentType(MediaType.APPLICATION_JSON)
	                .bodyValue(error);
		};
		
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(errorPropertiesMap);
    }
    
	private HttpStatus determineHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
		if (error instanceof ResponseStatusException) {
			return ((ResponseStatusException) error).getStatus();
		}
		return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
