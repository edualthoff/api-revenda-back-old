package br.edx.exception.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.config.msg.ApiErrorCode;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ApiBadRequestException extends RuntimeException implements ApiErrorCode {
	private static final long serialVersionUID = 992428721490241972L;

	private static final Logger log = LoggerFactory.getLogger(ApiBadRequestException.class);

	private String errorCode;
	private String messageError;
	
	/**
	 * Mensagme do error referente ao objeto
	 * @param message
	 */
	public ApiBadRequestException(String message) {
        super(message);
        this.errorCode = ApiMessageSource.toMessage("bad_request.error.code");
        this.messageError = ApiMessageSource.toMessage("bad_request.error.msg");
    }
	
	/**
	 * Codigo do error, mesagem padrao do error e a mensagem referente ao o objeto
	 * @param message
	 * @param errorCode
	 * @param messageError
	 */
	public ApiBadRequestException(String errorCode, String messageError, String message) {
		super(message);
		this.errorCode = errorCode;
		this.messageError = messageError;
	}

	/**
	 * Codigo do error e a mensagem referente ao o objeto
	 * @param message
	 * @param errorCode
	 * @param messageError
	 */
	public ApiBadRequestException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.messageError = ApiMessageSource.toMessage("bad_request.error.msg");
	}


	@Override
	public String getErrorCode() {
		log.debug("error code: "+ApiMessageSource.toMessage("bad_request.error.code"));
		return errorCode;
	}


	@Override
	public String getMessageError() {
		// TODO Auto-generated method stub
		return messageError;
	}
}
