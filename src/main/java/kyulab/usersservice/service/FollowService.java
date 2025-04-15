package kyulab.usersservice.service;

import kyulab.usersservice.dto.res.FollowDto;
import kyulab.usersservice.entity.Follow;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.BadRequestException;
import kyulab.usersservice.repository.FollowRepository;
import kyulab.usersservice.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final UsersService usersService;

	@Transactional(readOnly = true)
	public List<FollowDto> getFollows() {
		Users users = usersService.getUser(UserContext.getUserId());
		return followRepository.findFollowsByFollower(users).stream()
				.map(follow -> usersService.getUser(follow.getFollower().getId()))
				.map(FollowDto::from)
				.toList();
	}

	@Transactional
	public void followUser(long followerId) {
		long userId = UserContext.getUserId();
		if (followerId == userId) {
			throw new BadRequestException("자기 자신을 구독할 수 없습니다..");
		}

		Users follower = usersService.getUser(followerId);
		Users following = usersService.getUser(UserContext.getUserId());
		if (followRepository.existsByFollowerAndFollowing(follower, following)) {
			throw new BadRequestException("이미 구독하였습니다.");
		}
		Follow follow = new Follow(follower, following);
		followRepository.save(follow);
	}

	@Transactional
	public void unFollow(long followerId) {
		Users follower = usersService.getUser(followerId);
		Users following = usersService.getUser(UserContext.getUserId());
		followRepository.deleteByFollowerAndFollowing(follower, following);
	}

}
