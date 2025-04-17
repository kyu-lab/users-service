package kyulab.usersservice.handler.exception;

/**
 * 중복된 데이터 비허용시 사용
 */
public class ConflictRequestException extends RuntimeException {
	public ConflictRequestException(String message) {
		super(message);
	}
}
