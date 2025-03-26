package kyulab.usersservice.service;

import kyulab.usersservice.dto.gateway.UsersGroupCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupGatewayService {

	@Value("${gateway.url:}")
	private String gateway;

	@Value("${gateway.group-path:}")
	private String groupPath;

	private final RestTemplate restTemplate;

	/**
	 * 게시글 서비스(post-serivce)에게 사용자 개인 그룹 생성을 요청한다.
	 * @param userInfo 요청할 사용자 정보 목록
	 * @return 사용자 정보 목록
	 */
	public Boolean reqeusetPostUserGroup(UsersGroupCreateDto userInfo) {
		HttpEntity<UsersGroupCreateDto> request = new HttpEntity<>(userInfo);
		return restTemplate.exchange(
				gateway + groupPath,
				HttpMethod.POST,
				request,
				new ParameterizedTypeReference<Boolean>() {}
		).getBody();
	}

}
