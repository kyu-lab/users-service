package kyulab.usersservice.dto.gateway.req;

import java.util.Set;

/**
 * 요청 받을 사용자의 목록
 * @param requestUserId 요청한 사용자 아이디
 * @param usersIds 사용자 목록 (중복 X)
 */
public record UsersListGatewayReqDto(Long requestUserId, Set<Long> usersIds) {
}
