package sk.edenis.taskmanager.exception;

public class UserLoginException extends IllegalArgumentException {

	private static final long serialVersionUID = 1631673360139719141L;

	public UserLoginException(String message) {
		super(message);
	}
}
