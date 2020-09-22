package br.edx.exception.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.config.msg.ApiErrorCode;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ApiNotFoundException  extends RuntimeException implements ApiErrorCode {
	private static final long serialVersionUID = -6588825267056042318L;
	private static final Logger log = LoggerFactory.getLogger(ApiNotFoundException.class);

	private String errorCode;
	private String messageError;
	
	/**
	 * Mensagme do error referente ao objeto
	 * @param message
	 */
	public ApiNotFoundException(String message) {
        super(message);
        this.errorCode = ApiMessageSource.toMessage("400.error.code");
        this.messageError = ApiMessageSource.toMessage("400.error.msg");
    }
	
	/**
	 * Codigo do error, mesagem padrao do error e a mensagem referente ao o objeto
	 * @param message
	 * @param errorCode
	 * @param messageError
	 */
	public ApiNotFoundException(String errorCode, String messageError, String message) {
		super(message);
		this.errorCode = errorCode;
		this.messageError = messageError;
	}
	
	/**
	 * Codigo do error e a mensagem referente ao o objeto
	 * @param errorCode
	 * @param message
	 */
	public ApiNotFoundException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.messageError = ApiMessageSource.toMessage("400.error.msg");
	}
	

	@Override
	public String getErrorCode() {
		log.debug("error code: "+ApiMessageSource.toMessage("objeto.error.null.code"));
		return errorCode;
	}


	@Override
	public String getMessageError() {
		// TODO Auto-generated method stub
		return messageError;
	}

}
