package kyulab.usersservice.util;

import kyulab.usersservice.handler.exception.BadRequestException;

/**
 * 요청한 사용자 아이디 관리 객체 <br />
 * 파싱 위치는 {@link kyulab.usersservice.interceptor.ServiceInterceptor}를 참고.
 */
public class UserContext {
	private static final ThreadLocal<Long> userIdContext = new ThreadLocal<>();

	public static void setUserId(Long userId) {
		userIdContext.set(userId);
	}

	public static Long getUserId() {
		Long userId = userIdContext.get();
		if (userId == null) {
			throw new BadRequestException("Token cant be null");
		}
		return userId;
	}
}
