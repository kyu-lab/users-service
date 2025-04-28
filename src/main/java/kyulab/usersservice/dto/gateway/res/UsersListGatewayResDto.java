package kyulab.usersservice.dto.gateway.res;

import java.util.List;
import java.util.Set;

/**
 * 요청 받은 사용자 목록
 * @param userList 발견한 사용자 데이터들
 * @param failList 찾지 못한 사용자 이이디 목록
 */
public record UsersListGatewayResDto(List<UsersInfoGatewayResDto> userList, Set<Long> failList) {
}
