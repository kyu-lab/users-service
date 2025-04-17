package kyulab.usersservice.controller;

import kyulab.usersservice.dto.res.FollowInfoDto;
import kyulab.usersservice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;

	@GetMapping
	public ResponseEntity<List<FollowInfoDto>> getFollows() {
		return ResponseEntity.ok(followService.getFollows());
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
