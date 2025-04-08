package kyulab.usersservice.util;

import kyulab.usersservice.handler.exception.BadRequestException;

// 사용자 아이디 관리용
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
