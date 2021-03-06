/**
 * 
 */
package com.frost.documentservice.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.frost.documentservice.model.ApiError;

import lombok.extern.slf4j.Slf4j;

/**
 * ControllerAdvice that provides custom error response in case of any
 * Exceptions thrown by the Rest Controllers.
 * 
 * @author jobin
 *
 */
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ ValidationException.class })
	protected ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {

		log.error("ValidationException Occured!", ex);

		List<String> errors = new ArrayList<String>();
		ex.getValidationErrors().forEach((k, v) -> {
			errors.add(k + " : " + v);
		});

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("ServletRequestBindingException Occured!", ex);

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("MethodArgumentNotValidException Occured!", ex);

		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);

	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("HttpMessageNotReadableException Occured!", ex);

		String error = "Malformed JSON request ";
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error + ex.getLocalizedMessage());
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {

		log.error("MethodArgumentTypeMismatchException Occured!", ex);
		String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("MissingServletRequestParameterException Occured!", ex);
		String error = ex.getParameterName() + " parameter is missing ";

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

		log.error("ConstraintViolationException Occured!", ex);
		List<String> errors = new ArrayList<String>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		log.error("NoHandlerFoundException Occured!", ex);
		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("HttpRequestMethodNotSupportedException Occured!", ex);
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, builder.toString());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("HttpMediaTypeNotSupportedException Occured!", ex);
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

		ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2));
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		log.error("Internal Server Error Occured!", ex);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
				"Error occurred " + ex.getLocalizedMessage());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}
