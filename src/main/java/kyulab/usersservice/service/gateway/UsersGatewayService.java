package kyulab.usersservice.service.gateway;

import kyulab.usersservice.dto.gateway.req.UsersListGatewayReqDto;
import kyulab.usersservice.dto.gateway.res.UsersListGatewayResDto;
import kyulab.usersservice.dto.gateway.res.UsersInfoGatewayResDto;
import kyulab.usersservice.handler.exception.NotFoundException;
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
	public UsersInfoGatewayResDto getUser(long id) {
		return usersRepository.findUserWithFollow(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new NotFoundException("Inavlid User");
				});
	}

	@Transactional(readOnly = true)
	public UsersListGatewayResDto getUsers(UsersListGatewayReqDto listDto) {
		List<UsersInfoGatewayResDto> userList;
		if (listDto.requestUserId() == null) { // 비로그인 유저
			userList = usersRepository.findUsersWithFollow(listDto.usersIds());
		} else {
			userList = usersRepository.findUsersWithFollowForLogin(listDto.requestUserId(), listDto.usersIds());
		}

		Set<Long> foundUserIds = userList.stream()
				.map(UsersInfoGatewayResDto::id)
				.collect(Collectors.toSet());

		// 찾지 못한 사용자 ID 목록 생성
		Set<Long> failList = listDto.usersIds().stream()
				.filter(id -> !foundUserIds.contains(id))
				.collect(Collectors.toSet());

		return new UsersListGatewayResDto(userList, failList);
	}

}
