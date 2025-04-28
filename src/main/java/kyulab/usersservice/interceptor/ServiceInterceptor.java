package kyulab.usersservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyulab.usersservice.handler.exception.BadRequestException;
import kyulab.usersservice.handler.exception.ServerErrorExcpetion;
import kyulab.usersservice.service.TokenService;
import kyulab.usersservice.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 프론트의 요청시 토큰값을 파싱하여 사용자 아이디를 추출한다. <br/>
 * 로그인 유저인 경우는 {@link kyulab.usersservice.util.UserContext#getUserId()}에 아이디 저장 <br/>
 * 비로그인 유저는 아이디값 저장없이 서비스 진행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceInterceptor implements HandlerInterceptor {

	private final TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(token)) {
			String userId = tokenService.getUserId(token.substring(7));
			if (!userId.isEmpty()) {
				try {
					UserContext.setUserId(Long.parseLong(userId));
				} catch (NumberFormatException n) {
					log.error("파싱 과정 오류 발생");
					throw new ServerErrorExcpetion("서버 에러 발생");
				} catch (Exception e) {
					log.debug("에러 메시지 : {}, 토큰값 : {}", e, token);
					throw new BadRequestException("잘못된 요청 받음");
				}
			}
		}

		return true;
	}

}
