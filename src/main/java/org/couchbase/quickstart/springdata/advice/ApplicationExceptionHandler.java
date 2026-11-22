package org.couchbase.quickstart.springdata.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Single place that maps Couchbase / Spring Data exceptions to HTTP status codes
 * for every controller, so the mapping cannot drift between endpoints.
 * Spring picks the most specific matching handler, so not-found and conflict
 * errors win over the generic data-access handler.
 */
@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMap.put(fieldName, errorMessage);
        });
        
        return errorMap;
    }

    @ExceptionHandler({ DocumentNotFoundException.class, DataRetrievalFailureException.class })
    public ResponseEntity<Void> handleDocumentNotFound(Exception ex) {
        log.error("Document not found", ex);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ DocumentExistsException.class, DuplicateKeyException.class })
    public ResponseEntity<Void> handleDocumentExists(Exception ex) {
        log.error("Document already exists", ex);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ CouchbaseException.class, DataAccessException.class })
    public ResponseEntity<Void> handleDataAccessFailure(Exception ex) {
        log.error("Internal server error", ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
