package sk.edenis.taskmanager.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sk.edenis.taskmanager.controller.rest.TaskRestController;
import sk.edenis.taskmanager.dto.ErrorResponse;
import sk.edenis.taskmanager.exception.MissingFieldException;

import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {TaskRestController.class})
public class RestTaskExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestTaskExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        logger.error("No such element: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No Such Element", e.getMessage()));
    }

    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<ErrorResponse> handleMissingFieldException(MissingFieldException e) {
        logger.error("Missing field error: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("Missing Field", e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
		logger.error("Unexpected error: ", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse("Unexpected Task Error", "An unexpected error occurred. Please try again later."));
	}
}
