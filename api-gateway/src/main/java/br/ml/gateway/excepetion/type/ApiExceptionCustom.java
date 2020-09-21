package br.ml.gateway.excepetion.type;

import br.ml.gateway.excepetion.msg.ApiErrorCodeException;

public abstract class ApiExceptionCustom extends RuntimeException implements ApiErrorCodeException{
	private static final long serialVersionUID = -1443406053005136546L;
	

	public ApiExceptionCustom(String message) {
		super(message);
	}
}
