package kyulab.usersservice.controller;

import kyulab.usersservice.dto.res.UsersInfoResDTO;
import kyulab.usersservice.service.UsersService;
import kyulab.usersservice.common.BasicResponse;
import kyulab.usersservice.dto.req.UsersLoginReqDTO;
import kyulab.usersservice.dto.req.UsersSignUpReqDTO;
import kyulab.usersservice.dto.req.UsersUpdateReqDTO;
import kyulab.usersservice.dto.res.UsersLoginResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

	private final UsersService usersService;

	@GetMapping("/{id}")
	public ResponseEntity<BasicResponse<UsersInfoResDTO>> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(new BasicResponse<>(usersService.getUser(id)));
	}

	// todo : 테스트용
	@PostMapping("/test")
	public ResponseEntity<BasicResponse<UsersLoginResDTO>> test() {
		return ResponseEntity.ok(new BasicResponse<>("success"));
	}

	@PostMapping("/login")
	public ResponseEntity<BasicResponse<UsersLoginResDTO>> login(@RequestBody UsersLoginReqDTO loginReqDTO) {
		return ResponseEntity.ok(new BasicResponse<>(usersService.login(loginReqDTO)));
	}

	@PostMapping("/signUp")
	public ResponseEntity<BasicResponse<String>> signUp(@RequestBody UsersSignUpReqDTO signUpReqDTO) {
		usersService.signUp(signUpReqDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(new BasicResponse<>("Sign Up Success"));
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<BasicResponse<UsersInfoResDTO>> update(@PathVariable Long id, @RequestBody UsersUpdateReqDTO updateReqDTO) {
		return ResponseEntity.ok(new BasicResponse<>(usersService.update(id, updateReqDTO)));
	}

}
