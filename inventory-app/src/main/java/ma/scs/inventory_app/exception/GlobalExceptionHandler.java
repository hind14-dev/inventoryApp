package ma.scs.inventory_app.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RangeAlreadyAssignedException.class)
    public ResponseEntity<Map<String, String>> handleRangeAssigned(RangeAlreadyAssignedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "RangeConflict");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleGenericIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "DatabaseError");
        errorResponse.put("message", "A database error occurred. Please contact IT team.");
        // optionally log ex.getMessage() for debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



}

