package kyulab.usersservice.handler.exception;

/**
 * 서버 에러시 반환
 */
public class ServerErrorExcpetion extends RuntimeException {
	public ServerErrorExcpetion(String message) {
		super(message);
	}
}
