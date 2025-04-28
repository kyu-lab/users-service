package kyulab.usersservice.handler.exception;

/**
 * 다른 서비스와 요청 실패시 사용
 */
public class ServiceUnavailabeExcpetion extends RuntimeException {
	public ServiceUnavailabeExcpetion(String message) {
		super(message);
	}
}
