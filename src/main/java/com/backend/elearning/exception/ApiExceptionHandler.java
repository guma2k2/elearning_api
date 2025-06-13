package com.backend.elearning.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorVm> handleNotFoundException(NotFoundException ex, WebRequest request) {
        String message = ex.getMessage();
        ErrorVm errorVm = new ErrorVm(HttpStatus.NOT_FOUND.toString(), HttpStatus.NOT_FOUND.getReasonPhrase(), message);
        log.warn(ERROR_LOG_FORMAT, this.getServletPath(request), 404, message);
        log.debug(ex.toString());
        return new ResponseEntity<>(errorVm, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorVm> handleBadRequestException(BadRequestException ex, WebRequest request) {
        String message = ex.getMessage();
        ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(), message);
        return ResponseEntity.badRequest().body(errorVm);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorVm> handleBadRequestException(BadCredentialsException ex, WebRequest request) {
        String message = "Email hoặc mật khẩu không chính xác";
        ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(), message);
        return ResponseEntity.badRequest().body(errorVm);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorVm> handleBadRequestException(DuplicateException ex, WebRequest request) {
        String message = ex.getMessage();
        ErrorVm errorVm = new ErrorVm(HttpStatus.CONFLICT.toString(), HttpStatus.CONFLICT.getReasonPhrase(), message);
        return ResponseEntity.badRequest().body(errorVm);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVm> handleException(Exception ex, WebRequest request) {
        String message = ex.getMessage();
        ErrorVm errorVm = new ErrorVm(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), message);
        log.error(errorVm.toString());
        return ResponseEntity.badRequest().body(errorVm);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .toList();

        ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Request information is not valid", errors);
        return ResponseEntity.badRequest().body(errorVm);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Request information is not valid", errors);
        return ResponseEntity.badRequest().body(errorVm);
    }

    private String getServletPath(WebRequest webRequest) {
        ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
        return servletRequest.getRequest().getServletPath();
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorVm> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        // Handle Enum Deserialization specifically
        if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            String field = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
            String rejectedValue = String.valueOf(ife.getValue());
            String enumName = ife.getTargetType().getSimpleName();
            Object[] allowed = ife.getTargetType().getEnumConstants();

            List<String> fieldErrors = new ArrayList<>();
            fieldErrors.add(
                    String.format("Field '%s': invalid value '%s'. Expected one of: %s",
                            field,
                            rejectedValue,
                            String.join(", ", java.util.Arrays.stream(allowed).map(Object::toString).toList()))
            );

            ErrorVm error = new ErrorVm(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Invalid enum value for field '" + field + "'",
                    fieldErrors
            );

            return ResponseEntity.badRequest().body(error);
        }

        // Fallback for other bad JSON structures
        ErrorVm fallbackError = new ErrorVm(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid request body: " + ex.getMostSpecificCause().getMessage()
        );

        return ResponseEntity.badRequest().body(fallbackError);
    }
}
