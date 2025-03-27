package kyulab.usersservice.controller;

import kyulab.usersservice.dto.gateway.UsersList;
import kyulab.usersservice.dto.res.UsersInfoResDto;
import kyulab.usersservice.service.UsersGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gateway/users")
@RequiredArgsConstructor
public class UsersGatewayController {

	private final UsersGatewayService usersGatewayService;

	@GetMapping("/{id}")
	public UsersInfoResDto getUser(@PathVariable Long id) {
		return usersGatewayService.getUser(id);
	}

	@PostMapping
	public UsersList getUsers(@RequestBody List<Long> userIds) {
		return usersGatewayService.getUsers(userIds);
	}

}
