package kyulab.usersservice.handler.exception;

public class ConflictRequestException extends RuntimeException {
	public ConflictRequestException(String message) {
		super(message);
	}
}
