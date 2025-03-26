package sk.edenis.taskmanager.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(String message, String error, Map<String, String> errors) {

    public ErrorResponse(String message, String error) {
        this(message, error, null);
    }

    public ErrorResponse(String message, Map<String, String> errors) {
        this(message, null, errors);
    }

    public ErrorResponse(String message) {
        this(message, null, null);
    }
}