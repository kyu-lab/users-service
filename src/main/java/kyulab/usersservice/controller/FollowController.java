package kyulab.usersservice.controller;

import kyulab.usersservice.dto.res.UserFollowDto;
import kyulab.usersservice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;

	@GetMapping("/{userId}/follower")
	public ResponseEntity<UserFollowDto> getFollower(
			@PathVariable long userId,
			@RequestParam(required = false) Long cursor) {
		return ResponseEntity.ok(followService.getFollower(userId, cursor));
	}

	@GetMapping("/{userId}/following")
	public ResponseEntity<UserFollowDto> getFollowing(
			@PathVariable long userId,
			@RequestParam(required = false) Long cursor) {
		return ResponseEntity.ok(followService.getFollowing(userId, cursor));
	}

	@PostMapping("/{followerId}")
	public ResponseEntity<String> followUser(@PathVariable long followerId) {
		followService.followUser(followerId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{followerId}")
	public ResponseEntity<String> unFollowUser(@PathVariable long followerId) {
		followService.unFollow(followerId);
		return ResponseEntity.ok().build();
	}

}
