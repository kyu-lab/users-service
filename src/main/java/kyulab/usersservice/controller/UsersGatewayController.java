package kyulab.usersservice.controller;

import kyulab.usersservice.dto.res.UsersInfoResDTO;
import kyulab.usersservice.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway/users")
@RequiredArgsConstructor
public class UsersGatewayController {

	private final UsersService usersService;

	@GetMapping("/{id}")
	public UsersInfoResDTO getUser(@PathVariable Long id) {
		return usersService.getUser(id);
	}

}
