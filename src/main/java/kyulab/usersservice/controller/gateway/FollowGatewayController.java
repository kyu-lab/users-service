package kyulab.usersservice.controller.gateway;

import kyulab.usersservice.service.gateway.FollowGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gateway/follow")
@RequiredArgsConstructor
public class FollowGatewayController {

	private final FollowGatewayService followGatewayService;

	@GetMapping("/{id}")
	public List<Long> getFollows(@PathVariable long id) {
		return followGatewayService.getFollows(id);
	}

}
