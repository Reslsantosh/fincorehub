package com.resustainability.fincorehub.adviser;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.resustainability.fincorehub.exception.BaseException;
import com.resustainability.fincorehub.commons.APIResponse;
import com.resustainability.fincorehub.commons.CollectionUtils;
import com.resustainability.fincorehub.commons.Default;



@RestControllerAdvice
public class ExceptionAdviser {
	private final Logger log = LoggerFactory.getLogger(ExceptionAdviser.class);

	@ExceptionHandler(value = BaseException.class)
	public ResponseEntity<APIResponse<Object>> handleCustomExceptions(BaseException exception) {
		if (exception.isEmptyBody()) {
			return ResponseEntity
					.status(exception.getStatus())
					.build();
		} else {
			return ResponseEntity
					.status(exception.getStatus())
					.body(exception.getResponse());
		}
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException exception) {
		return ResponseEntity
				.status(exception.getStatusCode())
				.contentType(MediaType.APPLICATION_JSON)
				.body(exception.getResponseBodyAsString());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public APIResponse<Void> handleUnhandledExceptions(Exception exception) {
		log.error(Default.EMPTY, exception);
		return new APIResponse<>(null, null, "Uh-oh! Something went wrong. :(");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public APIResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		final String errorMessage = CollectionUtils.isBlank(exception.getFieldErrors())
				? exception.getMessage()
				: exception.getFieldErrors().get(0).getDefaultMessage();
		return new APIResponse<>(null, null, errorMessage);
	}

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public ResponseEntity<APIResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public APIResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		final StringBuilder errorMessageBuilder = new StringBuilder("Uh-oh! Request cannot be processed due to an invalid payload.");
		final Throwable cause = exception.getCause();
		if (cause instanceof JsonMappingException jsonMappingException) {
            final List<JsonMappingException.Reference> path = jsonMappingException.getPath();
			if (CollectionUtils.isBlank(path) || null == path.get(0) || null == path.get(0).getFieldName()) {
				errorMessageBuilder
						.append(" There is an issue with parsing the payload.");
			} else {
				errorMessageBuilder
						.append(" There is an issue with parsing the parameter named `")
						.append(path.get(0).getFieldName())
						.append("`.");
			}
		} else {
			errorMessageBuilder
					.append(" Please check your request payload and try again.");
		}
		return new APIResponse<>(null, null, errorMessageBuilder.toString());
	}


	@ExceptionHandler(value = NoResourceFoundException.class)
	public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = JpaSystemException.class)
	public APIResponse<Void> handleJpaSystemException(JpaSystemException exception) {
		final Throwable rootCause = exception.getRootCause();
		if (rootCause instanceof SQLException) {
			return defaultResponse(rootCause);
		} else {
			return defaultResponse(exception);
		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MultipartException.class)
	public APIResponse<Void> handleMultipartException(MultipartException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public APIResponse<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingPathVariableException.class)
	public APIResponse<Void> handleMissingPathVariableException(MissingPathVariableException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(MissingRequestHeaderException.class)
	public APIResponse<Void> handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(MissingServletRequestPartException.class)
	public APIResponse<Void> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
		return defaultResponse(exception);
	}

/*	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(BadCredentialsException.class)
	public APIResponse<Void> handleBadCredentialsException(BadCredentialsException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AuthorizationDeniedException.class)
	public APIResponse<Void> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AuthenticationServiceException.class)
	public APIResponse<Void> handleAuthenticationServiceException(AuthenticationServiceException exception) {
		return defaultResponse(exception);
	}*/




	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(ConnectException.class)
	public APIResponse<Void> handleCommunicationsException(ConnectException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	@ExceptionHandler(CannotAcquireLockException.class)
	public APIResponse<Void> handleCannotAcquireLockException(CannotAcquireLockException exception) {
		return new APIResponse<>("Your request cannot be processed due to too many attempts.");
	}

	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public APIResponse<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
		return defaultResponse(exception);
	}

	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public APIResponse<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		return defaultResponse(exception);
	}
	
	private APIResponse<Void> defaultResponse(Throwable throwable) {
		return new APIResponse<>(null, null, throwable.getMessage());
	}
}

