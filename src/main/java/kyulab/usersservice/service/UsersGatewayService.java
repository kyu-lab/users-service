package kyulab.usersservice.service;

import kyulab.usersservice.dto.gateway.UsersList;
import kyulab.usersservice.dto.res.UsersInfoResDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.UserNotFoundException;
import kyulab.usersservice.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
	@Cacheable("user")
	public UsersInfoResDto getUser(Long id) {
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("Inavlid User");
				});
		return UsersInfoResDto.from(users);
	}

	@Transactional(readOnly = true)
	public UsersList getUsers(List<Long> userIds) {
		List<Users> users = usersRepository.findByIdIn(userIds);

		Set<Long> foundUserIds = users.stream()
				.map(Users::getId)
				.collect(Collectors.toSet());

		// 찾지 못한 사용자 ID 목록 생성
		List<Long> failList = userIds.stream()
				.filter(id -> !foundUserIds.contains(id))
				.toList();

		List<UsersInfoResDto> userList = users.stream()
				.map(UsersInfoResDto::from)
				.toList();

		return new UsersList(userList, failList);
	}

}
