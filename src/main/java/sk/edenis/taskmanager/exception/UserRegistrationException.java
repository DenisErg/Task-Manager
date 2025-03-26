package sk.edenis.taskmanager.exception;

public class UserRegistrationException extends IllegalArgumentException {

	private static final long serialVersionUID = 7111097720532863762L;

	public UserRegistrationException(String message) {
		super(message);
	}
}
