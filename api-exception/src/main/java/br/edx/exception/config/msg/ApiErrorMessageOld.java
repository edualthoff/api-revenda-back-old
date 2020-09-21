package br.edx.exception.config.msg;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;


@EqualsAndHashCode
public class ApiErrorMessageOld implements Serializable{
	private static final long serialVersionUID = -4347406695182953672L;

	private final String apiVersion;
	private final ErrorBlock error;

	public ApiErrorMessageOld(final String apiVersion, final String code, final String message, final String domain,
	                             final String reason, final String errorMessage, final String errorReportUri) {
	        this.apiVersion = apiVersion;
	        this.error = new ErrorBlock(code, message, domain, reason, errorMessage, errorReportUri);
	    }

	public static ApiErrorMessageOld fromDefaultAttributeMap(final String apiVersion,
			final Map<String, Object> defaultErrorAttributes, final String sendReportBaseUri) {
		// original attribute values are documented in
		// org.springframework.boot.web.servlet.error.DefaultErrorAttributes
		return new ApiErrorMessageOld(apiVersion, 
				((Integer) defaultErrorAttributes.get("status")).toString(),
				(String) defaultErrorAttributes.getOrDefault("message", "no message available"),
				(String) defaultErrorAttributes.getOrDefault("path", "no domain available"),
				(String) defaultErrorAttributes.getOrDefault("error", "no reason available"),
				(String) defaultErrorAttributes.get("message"), sendReportBaseUri);
	}

	// utility method to return a map of serialized root attributes,
	// see the last part of the guide for more details
	public Map<String, Object> toAttributeMap() {
		return Map.of("apiVersion", apiVersion, "error", error);
	}
	// utility method to return a map of serialized root attributes,
	// see the last part of the guide for more details
	public Map<? extends String, ? extends Object> toAttributeMapToMap() {
		return Map.of("apiVersion", apiVersion, "error", Map.of("error", error));
	}
	public String getApiVersion() {
		return apiVersion;
	}

	public ErrorBlock getError() {
		return error;
	}

	@EqualsAndHashCode
	private static final class ErrorBlock {

		@JsonIgnore
		private final UUID uniqueId;
		private final String code;
		private final String message;
		private final Error errors;

		public ErrorBlock(final String code, final String message, final String domain, final String reason,
				final String errorMessage, final String errorReportUri) {
			this.code = code;
			this.message = message;
			this.uniqueId = UUID.randomUUID();
			this.errors = new Error(domain, reason, errorMessage, errorReportUri + "?id=" + uniqueId);
		}

		private ErrorBlock(final UUID uniqueId, final String code, final String message, final Error errors) {
			this.uniqueId = uniqueId;
			this.code = code;
			this.message = message;
			this.errors = errors;
		}
		@JsonGetter
		public static ErrorBlock copyWithMessage(final ErrorBlock s, final String message) {
			return new ErrorBlock(s.uniqueId, s.code, message, s.errors);
		}
		@JsonGetter
		public UUID getUniqueId() {
			return uniqueId;
		}
		@JsonGetter
		public String getCode() {
			return code;
		}
		@JsonGetter
		public String getMessage() {
			return message;
		}
		@JsonGetter
		public Error getErrors() {
			return errors;
		}

		@Override
		public String toString() {
			return "error [uniqueId=" + uniqueId + ", code=" + code + ", message=" + message + ", errors=" + errors
					+ "]";
		}
	}
	private static final class Error {
		private final String domain;
		private final String reason;
		private final String message;
		private final String sendReport;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime data;
		
		public Error(final String domain, final String reason, final String message, final String sendReport) {
			this.domain = domain;
			this.reason = reason;
			this.message = message;
			this.sendReport = sendReport;
			this.data = LocalDateTime.now();
		}
		@JsonGetter
		public String getDomain() {
			return domain;
		}
		@JsonGetter
		public String getReason() {
			return reason;
		}
		@JsonGetter
		public String getMessage() {
			return message;
		}
		@JsonGetter
		public String getSendReport() {
			return sendReport;
		}
		@JsonGetter
		public LocalDateTime getData() {
			return data;
		}
		@Override
		public String toString() {
			return "errors [domain=" + domain + ", reason=" + reason + ", message=" + message + ", sendReport="
					+ sendReport + ", data=" + data + "]";
		}
		
	}
	

}
