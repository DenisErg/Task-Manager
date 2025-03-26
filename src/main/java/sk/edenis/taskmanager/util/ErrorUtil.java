package sk.edenis.taskmanager.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import sk.edenis.taskmanager.dto.ErrorResponse;

public class ErrorUtil {

	/**
	 * Formats error messages from the BindingResult and creates an ErrorResponseDTO.
	 * 
	 * This method processes all errors from the BindingResult and formats them into a response. If there is only one error, 
	 * it returns the message as a simple string. In case of multiple errors, the result is a map that associates field names 
	 * with their respective error messages.
	 * 
	 * @param errorMessage The main error message that will be included along with the error details.
	 * @param bindingResult The result of validation containing all the detected errors.
	 * @return ErrorResponseDTO An object containing the error message and the details of the errors, depending on the type of error.
	 * 
	 * @see ErrorResponse
	 */
	public static ErrorResponse formatErrorMessage(String errorMessage, BindingResult bindingResult) {
		Map<String, String> errors = new HashMap<>();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		if (fieldErrors.size() == 1) {
			return new ErrorResponse(errorMessage, fieldErrors.get(0).getDefaultMessage());
		}

		fieldErrors.stream().filter(
				fieldError -> fieldError.getDefaultMessage() != null && !fieldError.getDefaultMessage().isEmpty())
				.forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

		return new ErrorResponse(errorMessage, errors);
	}
	
	public static ErrorResponse formatErrorMessage(String errorMessage) {
		return new ErrorResponse(errorMessage);
	}
	
	/**
	 * Extracts a user-friendly error message from a JSON error response body.
	 *
	 * This method attempts to parse the error body returned by the backend (usually in JSON format)
	 * and extract validation or general error messages. If the JSON contains a field named "errors",
	 * it will iterate through each field and collect the individual messages into a single string,
	 * separated by HTML line breaks ("<br>"). If no "errors" field is found, it will attempt to
	 * return the value of the "error" or "message" field. In case the JSON is not parseable, it will
	 * return a generic fallback message.
	 *
	 * @param errorBody The raw JSON string received as the error response body.
	 * @return A formatted error message string suitable for frontend display.
	 */
	public static String extractErrorMessage(String errorBody) {
	    try {
	        JsonNode root = new ObjectMapper().readTree(errorBody);

	        if (root.has("errors")) {
	            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(root.get("errors").fields(), 0), false)
	                    .map(entry -> entry.getValue().asText())
	                    .collect(Collectors.joining("<br>"));
	        }

	        return root.path("error").asText(root.path("message").asText("Unknown error"));

	    } catch (Exception e) {
	        return "Unexpected error format";
	    }
	}
}
