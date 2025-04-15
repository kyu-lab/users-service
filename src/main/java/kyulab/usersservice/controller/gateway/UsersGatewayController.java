package kyulab.usersservice.controller.gateway;

import kyulab.usersservice.dto.gateway.UsersList;
import kyulab.usersservice.dto.gateway.UsersListDto;
import kyulab.usersservice.dto.res.UsersInfoResDto;
import kyulab.usersservice.service.gateway.UsersGatewayService;
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
	public UsersList getUsers(@RequestBody UsersListDto listDto) {
		return usersGatewayService.getUsers(listDto);
	}

}
