package kyulab.usersservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyulab.usersservice.service.TokenService;
import kyulab.usersservice.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ServiceInterceptor implements HandlerInterceptor {

	private final TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 토큰이 있을 경우 아이디를 추출한다.
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(token)) {
			String userId = tokenService.getUserId(request.getHeader(HttpHeaders.AUTHORIZATION));
			if (!userId.isEmpty()) {
				UserContext.setUserId(Long.parseLong(userId));
			}
		}

		return true;
	}

}
