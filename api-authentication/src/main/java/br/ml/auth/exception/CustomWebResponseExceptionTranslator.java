package br.ml.auth.exception;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import br.edx.exception.config.ApiMessageSource;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

	@Value("${api.version}")
	private String currentApiVersion;
	@Value("${api.sendreport.uri}")
	private String sendReportUri;

	/**
	 * Modify OAuth2.0 Error Response
	 * 
	 * @param e
	 * @return ResponseEntity<OAuth2Exception>
	 * @throws Exception
	 */

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		ResponseEntity<?> responseEntity = super.translate(e);
		OAuth2Exception auth2Exception = (OAuth2Exception) responseEntity.getBody();
		if (auth2Exception != null) {
			switch (auth2Exception.getMessage()) {
			case "User is disabled": {
				System.out.println("user is ddd");
				//auth2Exception.create("invalid_grant", ApiMessageSource.toMessage("bad_request.error.msg.oauth.user-disabled"));
				auth2Exception.addAdditionalInformation("code", ApiMessageSource.toMessage("bad_request.error.code.oauth.user-disabled"));
				auth2Exception.addAdditionalInformation("message", ApiMessageSource.toMessage("bad_request.error.msg.oauth.user-disabled"));
				auth2Exception.addAdditionalInformation("apiVersion", this.currentApiVersion);
				break;
			}
			case "Bad credentials": {
				//auth2Exception.create("invalid_grant", ApiMessageSource.toMessage("bad_request.error.msg.oauth.user-disabled"));
				auth2Exception.addAdditionalInformation("code", ApiMessageSource.toMessage("bad_request.error.code.oauth.user-disabled"));
				auth2Exception.addAdditionalInformation("message", ApiMessageSource.toMessage("bad_request.error.cadastroVerificar.msg"));
				auth2Exception.addAdditionalInformation("apiVersion", this.currentApiVersion);
				break;
			}
			default:
				auth2Exception.addAdditionalInformation("apiVersion", this.currentApiVersion);
				break;
			}

		}
		return new ResponseEntity<OAuth2Exception>(auth2Exception, responseEntity.getHeaders(),
				responseEntity.getStatusCode());
	}

	/*
	private ApiErrorMessage apiErrorMessage(String codeError, String reason, String error, String uri) {
		return new ApiErrorMessage(
				this.currentApiVersion, 
				codeError,
				error,
				uri, 
				reason,
				ApiMessageSource.toMessage("bad_request.error.msg"),
				this.sendReportUri);
	}*/
}
