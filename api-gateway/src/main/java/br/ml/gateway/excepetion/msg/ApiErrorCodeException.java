package br.ml.gateway.excepetion.msg;

import org.springframework.http.HttpStatus;

public interface ApiErrorCodeException {
    /**
     * Provides an app-specific error code to help find out exactly what happened.
     * It's a human-friendly identifier for a given exception.
     *
     * @return a short text code identifying the error
     */
    String getErrorCode();
    
    String getMessageError();
    
    HttpStatus getStatus();
}
