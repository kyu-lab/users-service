package kyulab.usersservice.service.gateway;

import kyulab.usersservice.entity.Follow;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.repository.FollowRepository;
import kyulab.usersservice.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowGatewayService {

	private final FollowRepository followRepository;
	private final UsersService usersService;

	@Transactional(readOnly = true)
	public List<Long> getFollows(long id) {
		Users users = usersService.getUser(id);
		return followRepository.findFollowsByFollower(users).stream()
				.map(Follow::getFollowing)
				.map(Users::getId)
				.toList();
	}


}
