package br.com.sidney.alura_challenge_backend.exceptions.handler;

import br.com.sidney.alura_challenge_backend.exceptions.ResourceNotFoundException;
import br.com.sidney.alura_challenge_backend.exceptions.ValidationException;
import br.com.sidney.alura_challenge_backend.exceptions.model.ApiError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Class based on a ResponseEntityExceptionHandler that provides centralized exception
 * handling in all @RequestMapping methods through @ExceptionHandler methods.
 *
 * @author Sidney Miranda
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Customize the response for ResourceNotFoundException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        List<String> err = new ArrayList();
        err.add(ex.getMessage());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Resource Not Found")
                .status(HttpStatus.BAD_REQUEST.value())
                .className(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .errors(err)
                .build());
    }

    /**
     * Customize the response for ValidationException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        List<String> err = new ArrayList();
        err.add(ex.getMessage());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Constraint Infringed, check the documentation")
                .status(HttpStatus.BAD_REQUEST.value())
                .className(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .errors(err)
                .build());
    }

    /**
     * Customize the response for DateTimeParseException.
     *
     * @param ex    the exception
     * @return a {@Code ResponseEntity} instance
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex) {
        List<String> err = new ArrayList();
        err.add(ex.getMessage());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Date pattern incorrect")
                .status(HttpStatus.BAD_REQUEST.value())
                .className(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .errors(err)
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
        List<String> err = new ArrayList();
        err.add(ex.getMessage());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Constraint Violation")
                .status(HttpStatus.BAD_REQUEST.value())
                .className(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .errors(err)
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
        List<String> err = new ArrayList();
        err.add(ex.getMessage());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("No Such Element")
                .status(HttpStatus.BAD_REQUEST.value())
                .className(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .errors(err)
                .build());
    }

    /**
     * Customize the response for HttpMessageNotReadableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> details = new ArrayList();
        details.add(ex.getMessage());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Malformed JSON request")
                .status(HttpStatus.BAD_REQUEST.value())
                .className(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .errors(details)
                .build());
    }

    /**
     * Customize the response for MethodArgumentNotValidException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntityBuilder.build(ApiError
                .builder()
                .message("Validation Errors")
                .status(HttpStatus.BAD_REQUEST.value())
                .className(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .errors(details)
                .build());
    }
}
