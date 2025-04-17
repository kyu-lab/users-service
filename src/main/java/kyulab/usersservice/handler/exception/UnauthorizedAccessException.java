package kyulab.usersservice.handler.exception;

/**
 * 액세스 접근(ex: 토큰 없음) 못할 경우 사용
 */
public class UnauthorizedAccessException extends RuntimeException {
	public UnauthorizedAccessException(String message) {
		super(message);
	}
}
