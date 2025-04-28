package kyulab.usersservice.controller.gateway;

import kyulab.usersservice.dto.gateway.req.UsersListGatewayReqDto;
import kyulab.usersservice.dto.gateway.res.UsersListGatewayResDto;
import kyulab.usersservice.dto.gateway.res.UsersInfoGatewayResDto;
import kyulab.usersservice.service.gateway.UsersGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gateway/users")
@RequiredArgsConstructor
public class UsersGatewayController {

	private final UsersGatewayService usersGatewayService;

	@GetMapping("/{id}")
	public UsersInfoGatewayResDto getUser(@PathVariable long id) {
		return usersGatewayService.getUser(id);
	}

	@PostMapping
	public UsersListGatewayResDto getUsers(@RequestBody UsersListGatewayReqDto listDto) {
		return usersGatewayService.getUsers(listDto);
	}

}
