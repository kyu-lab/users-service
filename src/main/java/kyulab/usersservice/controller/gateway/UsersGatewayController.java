package kyulab.usersservice.controller.gateway;

import kyulab.usersservice.dto.gateway.res.UsersListGatewayDto;
import kyulab.usersservice.service.gateway.UsersGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gateway/users")
@RequiredArgsConstructor
public class UsersGatewayController {

	private final UsersGatewayService usersGatewayService;

	@PostMapping
	public UsersListGatewayDto getUsers(@RequestBody kyulab.usersservice.dto.gateway.req.UsersListGatewayDto listDto) {
		return usersGatewayService.getUsers(listDto);
	}

}
