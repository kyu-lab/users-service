package kyulab.usersservice.handler.exception;

/**
 * 잘못된 요청시 사용
 */
public class BadRequestException extends RuntimeException {
	public BadRequestException(String message) {
		super(message);
	}
}
