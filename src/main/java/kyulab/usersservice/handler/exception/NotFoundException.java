package kyulab.usersservice.handler.exception;

/**
 * 요청 대상을 못 찾을 경우
 */
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
