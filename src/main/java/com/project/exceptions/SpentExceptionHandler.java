package com.project.exceptions;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class SpentExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = Logger.getLogger(SpentExceptionHandler.class);

    @ExceptionHandler(com.project.exceptions.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleNoSuchEntityException(com.project.exceptions.EntityNotFoundException ex) {
        SpentError spentError = new SpentError(HttpStatus.NOT_FOUND);
        spentError.setMessage(ex.getMessage());
        return buildResponseEntity(spentError);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new SpentError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new SpentError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SpentError spentError = new SpentError(HttpStatus.BAD_REQUEST);
        spentError.setMessage("Validation error");
        spentError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        spentError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(spentError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        SpentError spentError = new SpentError(HttpStatus.BAD_REQUEST);
        spentError.setMessage("Validation error");
        spentError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(spentError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new SpentError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        logger.info("method : " + servletWebRequest.getHttpMethod() + " path : " + servletWebRequest.getRequest().getServletPath());
        String error = "Error writing JSON output";
        return buildResponseEntity(new SpentError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponseEntity(new SpentError(HttpStatus.NOT_FOUND, ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleIncorrectFields(BadCredentialsException ex) {
        return buildResponseEntity(new SpentError(HttpStatus.UNAUTHORIZED, ex));
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOExceptions(IOException ex) {
        return buildResponseEntity(new SpentError(HttpStatus.FORBIDDEN, ex));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SpentError spentError = new SpentError(HttpStatus.BAD_REQUEST);
        spentError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        spentError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(spentError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        SpentError spentError = new SpentError(HttpStatus.BAD_REQUEST);
        spentError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
        spentError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(spentError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new SpentError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new SpentError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(FileUploadException.class)
    protected ResponseEntity<Object> handleFileUploadException(FileUploadException ex, WebRequest request) {
        SpentError spentError = new SpentError(HttpStatus.BAD_REQUEST);
        spentError.setMessage(ex.getMessage());
        return buildResponseEntity(spentError);
    }

    @ExceptionHandler(FileNotFoundException.class)
    protected ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        SpentError spentError = new SpentError(HttpStatus.NOT_FOUND);
        spentError.setMessage(ex.getMessage());
        return buildResponseEntity(spentError);
    }

    private ResponseEntity<Object> buildResponseEntity(SpentError spentError) {
        return new ResponseEntity<>(spentError, spentError.getStatus());
    }
}
