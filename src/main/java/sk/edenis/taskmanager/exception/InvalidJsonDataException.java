package sk.edenis.taskmanager.exception;

public class InvalidJsonDataException extends RuntimeException{

	private static final long serialVersionUID = 7171842043321262489L;

	public InvalidJsonDataException(String message) {
		super(message);
	}
}
