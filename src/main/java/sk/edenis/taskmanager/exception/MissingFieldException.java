package sk.edenis.taskmanager.exception;

public class MissingFieldException extends RuntimeException {

	private static final long serialVersionUID = -5917606180511508923L;

	public MissingFieldException(String field) {
		super(field + " is invalid or missing.");
	}

}
