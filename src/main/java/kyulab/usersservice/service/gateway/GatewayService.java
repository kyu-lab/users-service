package kyulab.usersservice.service.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {

	@Value("${gateway.base-url:}")
	private String gateway;

	@Value("${gateway.post-path:/post}")
	private String postPath;

	private final RestTemplate restTemplate;

	/**
	 * 게시글 서비스(post-serivce)에게 사용자의 게시물 갯수를 요청한다.
	 * @param userId 데이터를 받을 사용자 목록
	 * @return 사용자가 작성한 게시물 갯수
	 */
	public long requestPostCount(long userId) {
		return restTemplate.getForEntity(gateway + postPath + "/" + userId, Long.class).getBody();
	}

}
