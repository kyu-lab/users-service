package kyulab.usersservice.controller.gateway;

import kyulab.usersservice.service.gateway.FollowGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/gateway/follow")
@RequiredArgsConstructor
public class FollowGatewayController {

	private final FollowGatewayService followGatewayService;

	@GetMapping("/{id}")
	public Set<Long> getFollows(@PathVariable long id) {
		return followGatewayService.getFollows(id);
	}

}
