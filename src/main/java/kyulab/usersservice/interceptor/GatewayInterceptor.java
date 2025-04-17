package kyulab.usersservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 마이크로 서비스간 REST 요청시 사용, X-GATE-WAY-KEY 값으로 검증한다.
 */
@Component
@RequiredArgsConstructor
public class GatewayInterceptor implements HandlerInterceptor {

	@Value("${gateway.gateway-key:}")
	private String gatewayKey;

	@Override
	public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
		String requestKey = request.getHeader("X-GATE-WAY-KEY");

		if (!gatewayKey.equals(requestKey)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write("Invalid gateway key");
			return false;
		}

		return true;
	}

}
