package br.edx.exception.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edx.exception.config.msg.ApiErrorMessage;
import br.edx.exception.type.ApiBadRequestException;
import br.edx.exception.type.ApiNotFoundException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiControllerAdvice {
	private static final Logger log = LoggerFactory.getLogger(ApiControllerAdvice.class);

	@Value("${api.version}")
	private String currentApiVersion;
	@Value("${api.sendreport.uri}")
	private String sendReportUri;
	@Autowired
	private HttpServletRequest request;
	
	@ExceptionHandler({ ApiNotFoundException.class })
	public ResponseEntity<ApiErrorMessage> handleNotFoundException(ApiNotFoundException e) {
		return errorRequest(HttpStatus.NOT_FOUND,
				new ApiErrorMessage(this.currentApiVersion, 
						e.getErrorCode(),
						e.getMessageError(), 
						this.request.getRequestURI().toString(),
						e.getMessage(), 
						ApiMessageSource.toMessage("msg.padrao"), 
						sendReportUri
						)
				);
	}

	@ExceptionHandler({ ApiBadRequestException.class })
	public ResponseEntity<ApiErrorMessage> handleBadRequestException(ApiBadRequestException e) {
		return errorRequest(HttpStatus.BAD_REQUEST,
				new ApiErrorMessage(this.currentApiVersion, 
						e.getErrorCode(),
						e.getMessageError(), 
						this.request.getRequestURI().toString(),
						e.getMessage(), 
						ApiMessageSource.toMessage("msg.padrao"), 
						sendReportUri
						)
				);
	}

	
	/*
	 * @ExceptionHandler({ApiServiceException.class}) public
	 * ResponseEntity<ApiErrorMessage>
	 * handleDogsServiceException(ApiServiceException e){ return
	 * error(HttpStatus.INTERNAL_SERVER_ERROR, e); }
	 */
	
	public ResponseEntity<ApiErrorMessage> errorRequest(HttpStatus status, ApiErrorMessage error) {
		log.error("Exception : ", error, status);
		return new ResponseEntity<>(error, status);
	}
}
