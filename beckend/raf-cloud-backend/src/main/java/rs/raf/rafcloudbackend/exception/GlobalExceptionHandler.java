package rs.raf.rafcloudbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> handleRuntime(RuntimeException ex) {
    String msg = ex.getMessage();

    if ("Unauthorized".equalsIgnoreCase(msg)
      || "Invalid credentials".equalsIgnoreCase(msg)) {
      return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(error(msg));
    }

    if ("Forbidden".equalsIgnoreCase(msg)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(error(msg));
    }

    if ("User not found".equalsIgnoreCase(msg)) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(error(msg));
    }

    if ("Email already exists".equalsIgnoreCase(msg)) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(error(msg));
    }

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(error("Internal server error"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors()
      .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(errors);
  }

  private Map<String, String> error(String message) {
    Map<String, String> map = new HashMap<>();
    map.put("error", message);
    return map;
  }


  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<?> handleOptimistic(ObjectOptimisticLockingFailureException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(Map.of("error", "Conflict: resource was updated by someone else"));
  }
}
