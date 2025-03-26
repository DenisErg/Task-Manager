package sk.edenis.taskmanager.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import sk.edenis.taskmanager.controller.rest.AuthenticationRestController;
import sk.edenis.taskmanager.dto.ErrorResponse;
import sk.edenis.taskmanager.exception.UserLoginException;
import sk.edenis.taskmanager.exception.UserRegistrationException;

@RestControllerAdvice(assignableTypes = {AuthenticationRestController.class})
public class RestAuthExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(RestAuthExceptionHandler.class);

	@ExceptionHandler(UserRegistrationException.class)
	public ResponseEntity<ErrorResponse> handleUserRegistrationException(UserRegistrationException e) {
		logger.error("Registration failed: {}", e.getMessage());
		return ResponseEntity.badRequest().body(new ErrorResponse("Registration Failed", e.getMessage()));
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        logger.error("User exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is Unauthorized"));
    }

	@ExceptionHandler(UserLoginException.class)
	public ResponseEntity<ErrorResponse> handleUserLoginException(UserLoginException e) {
		logger.error("Login failed: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Login Failed", e.getMessage()));
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        logger.error("JWT Token expired: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("JWT Expired", "Your session has expired. Please log in again."));
    }
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
		logger.error("Unexpected error: ", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse("Unexpected Auth Error", "An unexpected error occurred. Please try again later."));
	}
}
