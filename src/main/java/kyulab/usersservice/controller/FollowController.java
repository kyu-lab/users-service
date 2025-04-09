package kyulab.usersservice.controller;

import kyulab.usersservice.dto.res.FollowResDto;
import kyulab.usersservice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;

	@PostMapping("/login")
	public ResponseEntity<List<FollowResDto>> getFollows() {
		return ResponseEntity.ok(followService.getFollows());
	}

}
