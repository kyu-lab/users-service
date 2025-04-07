package kyulab.usersservice.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kyulab.usersservice.dto.req.UsersChangePasswordReqDto;
import kyulab.usersservice.dto.res.TokenDto;
import kyulab.usersservice.dto.req.UsersSignUpReqDto;
import kyulab.usersservice.dto.res.UsersInfoResDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.service.TokenService;
import kyulab.usersservice.service.UsersService;
import kyulab.usersservice.dto.req.UsersLoginReqDto;
import kyulab.usersservice.dto.req.UsersUpdateReqDto;
import kyulab.usersservice.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

	private final UsersService usersService;
	private final TokenService tokenService;
	private final StringRedisTemplate redisTemplate;

	@Value("${jwt.refresh-expiredTime:36000}")
	private Long refreshExpiredTime;

	@Value("${jwt.httpOnly:true}")
	private boolean tokenHttpOnly;

	@Value("${jwt.secure:true}")
	private boolean tokenSecure;

	@GetMapping("/mail/{email}/check")
	public ResponseEntity<String> checkEmail(
			@PathVariable("email")
			@NotBlank(message = "이메일은 필수입니다.")
		 	@Email(
			 	regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
			 	message = "유효한 이메일 형식이어야 합니다."
			)
		 	String mail) {

		if (usersService.existsByEmail(mail)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 이메일입니다.");
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/mail/{email}/exists")
	public ResponseEntity<String> existsEmail(
			@PathVariable("email")
			@NotBlank(message = "이메일은 필수입니다.")
			@Email(
					regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
					message = "유효한 이메일 형식이어야 합니다."
			)
			String mail) {
		if (!usersService.existsByEmail(mail)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 이메일입니다.");
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/name/{name}/check")
	public ResponseEntity<String> checkName(
			@PathVariable("name")
			@NotBlank(message = "이름은 필수 입력 항목입니다.")
			@Pattern(
				regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,29}$",
				message = "사용자 이름은 영문자로 시작해야 하며, 영문, 숫자, '_', '-', 만 사용할 수 있습니다."
			)
			String name) {
		if (usersService.existsByName(name)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 이름입니다.");
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/refresh")
	public ResponseEntity<TokenDto> refresh() {
		Long userId = UserContext.getUserId();
		String newAccessToken = tokenService.createToken(usersService.getUser(userId), true);
		return ResponseEntity.ok(new TokenDto(newAccessToken));
	}

	@GetMapping("/settings/account")
	public ResponseEntity<UsersInfoResDto> getUserAccount() {
		return ResponseEntity.ok(usersService.getUserInfo());
	}

	// todo : 사용자 옵션들...
	@GetMapping("/settings/notices")
	public ResponseEntity<String> getUserNotices() {
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<TokenDto> login(@RequestBody UsersLoginReqDto loginReqDTO) {
		Users users = usersService.login(loginReqDTO);
		String refreshToken = tokenService.createToken(users, false);
		redisTemplate.opsForValue().set("refresh-" + users.getId(), refreshToken, refreshExpiredTime, TimeUnit.SECONDS);

		// 리프레쉬 토큰 발급
		String cookie = ResponseCookie
				.from("refresh-token")
				.value(refreshToken)
				.httpOnly(tokenHttpOnly)
				.secure(tokenSecure)
				.maxAge(refreshExpiredTime)
				.path("/")
				.build()
				.toString();
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie)
				.body(new TokenDto(tokenService.createToken(users, true)));
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody UsersSignUpReqDto signUpReqDTO) {
		usersService.signup(signUpReqDTO);
		return ResponseEntity.ok("회원가입을 완료되었습니다.");
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String accessToken) {
		if (Objects.isNull(accessToken) || !accessToken.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token not includ");
		}
		redisTemplate.delete("refresh-" + UserContext.getUserId());

		// 리프레쉬 토큰 제거
		String cookie = ResponseCookie
				.from("refresh-token")
				.value("")
				.httpOnly(tokenHttpOnly)
				.secure(tokenSecure)
				.maxAge(0)
				.path("/")
				.build()
				.toString();
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie)
				.build();
	}

	@PostMapping("/change/password")
	public ResponseEntity<String> changePassword(@RequestBody UsersChangePasswordReqDto passwordReqDto) {
		usersService.changePassword(passwordReqDto);
		return ResponseEntity.ok("비밀번호가 재설정되었습니다.");
	}

	@PutMapping("/update")
	public ResponseEntity<UsersInfoResDto> update(@RequestBody UsersUpdateReqDto updateReqDTO) {
		return ResponseEntity.ok(usersService.update(updateReqDTO));
	}

	@DeleteMapping
	public ResponseEntity<String> deleteUser() {
		usersService.delete();
		return ResponseEntity.ok().build();
	}

}
