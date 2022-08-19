package br.com.sidney.alura_challenge_backend.exceptions.handler;

import br.com.sidney.alura_challenge_backend.exceptions.ResourceNotFoundException;
import br.com.sidney.alura_challenge_backend.exceptions.ValidationException;
import br.com.sidney.alura_challenge_backend.exceptions.model.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Class based on a ResponseEntityExceptionHandler that provides centralized exception
 * handling in all @RequestMapping methods through @ExceptionHandler methods.
 *
 * @author Sidney Miranda
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Customize the response for ResourceNotFoundException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Resource Not Found")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.asList(ex.getMessage()))
                .build());
    }

    /**
     * Customize the response for ValidationException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.asList(ex.getMessage()))
                .build());
    }

    /**
     * Customize the response for DateTimeParseException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Date pattern incorrect")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.asList(ex.getMessage()))
                .build());
    }

    /**
     * Customize the response for DataIntegrityViolationException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DataIntegrityViolationException ex) {
        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Cannot parse date")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.asList(ex.getMessage()))
                .build());
    }

    /**
     * Customize the response for ConstraintViolationException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Constraint Violation")
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.asList(ex.getMessage()))
                .build());
    }

    /**
     * Customize the response for DataIntegrityViolationException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleDateTimeParseException(NoSuchElementException ex) {
        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("No Such Element")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.asList(ex.getMessage()))
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Malformed JSON request")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.asList(ex.getMessage()))
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getObjectName() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Bad Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(details)
                .build());
    }
}
