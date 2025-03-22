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
		UsersInfoResDTO result = usersService.getUser(id);
		return ResponseEntity.ok(new BasicResponse<>(result));
	}

	// todo : 테스트용
	@PostMapping("/test")
	public ResponseEntity<BasicResponse<String>> test() {
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

	@PostMapping("/logout")
	public ResponseEntity<BasicResponse<String>> logout(@RequestBody Long id) {
		usersService.logout(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(new BasicResponse<>("Sign Up Success"));
	}

	@PostMapping("/refresh")
	public ResponseEntity<BasicResponse<UsersLoginResDTO>> refresh(@RequestParam Long userId) {
		return ResponseEntity.ok(new BasicResponse<>(usersService.refresh(userId)));
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<BasicResponse<UsersInfoResDTO>> update(@PathVariable Long id, @RequestBody UsersUpdateReqDTO updateReqDTO) {
		UsersInfoResDTO result = usersService.update(id, updateReqDTO);
		return ResponseEntity.ok(new BasicResponse<>(result));
	}

}
