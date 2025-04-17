package kyulab.usersservice.service.gateway;

import kyulab.usersservice.dto.gateway.res.UsersListGatewayDto;
import kyulab.usersservice.dto.gateway.res.UsersPreviewGatewayDto;
import kyulab.usersservice.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersGatewayService {

	private final UsersRepository usersRepository;

	@Transactional(readOnly = true)
	public UsersListGatewayDto getUsers(kyulab.usersservice.dto.gateway.req.UsersListGatewayDto listDto) {
		List<UsersPreviewGatewayDto> userList;
		if (listDto.requestUserId() == null) { // 비로그인 유저
			userList = usersRepository.findUserWithFollow(listDto.usersIds());
		} else {
			userList = usersRepository.findUserWithFollowForLogin(listDto.requestUserId(), listDto.usersIds());
		}

		Set<Long> foundUserIds = userList.stream()
				.map(UsersPreviewGatewayDto::id)
				.collect(Collectors.toSet());

		// 찾지 못한 사용자 ID 목록 생성
		Set<Long> failList = listDto.usersIds().stream()
				.filter(id -> !foundUserIds.contains(id))
				.collect(Collectors.toSet());

		return new UsersListGatewayDto(userList, failList);
	}

}
