package kyulab.usersservice.service;

import kyulab.usersservice.dto.res.FollowResDto;
import kyulab.usersservice.entity.Follow;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.repository.FollowRepository;
import kyulab.usersservice.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final UsersService usersService;

	public List<FollowResDto> getFollows() {
		long userId = UserContext.getUserId();
		Users users = usersService.getUser(userId);
		List<Follow> follows = followRepository.findFollowsByFollower(users);
		return new ArrayList<>();
	}


}
