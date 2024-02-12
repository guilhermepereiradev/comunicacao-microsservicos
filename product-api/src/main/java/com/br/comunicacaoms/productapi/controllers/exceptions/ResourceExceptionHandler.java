package com.br.comunicacaoms.productapi.controllers.exceptions;

import com.br.comunicacaoms.productapi.services.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(EntityNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(error)
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(standardError);
    }

//    @ExceptionHandler(BusinessRuleException.class)
//    public ResponseEntity<StandardError> businessException(BusinessRuleException e, HttpServletRequest request) {
//        String error = "Business rule exception";
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        StandardError standardError = StandardError.builder()
//                .timestamp(Instant.now())
//                .status(status.value())
//                .error(error)
//                .message(e.getMessage())
//                .path(request.getRequestURI())
//                .build();
//
//        return ResponseEntity.status(status).body(standardError);
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String error = "Invalid request";
        String message = "One or more fields are invalid. Please fill in the correct information and try again.";

        List<StandardError.Object> problemsObjects = ex.getBindingResult().getAllErrors().stream().map(err ->
                        StandardError.Object.builder()
                                .name(((FieldError) err).getField())
                                .userMessage(err.getDefaultMessage())
                                .build())
                .toList();

        StandardError standardError = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .objects(problemsObjects)
                .build();

        return ResponseEntity.status(status).body(standardError);
    }
}
