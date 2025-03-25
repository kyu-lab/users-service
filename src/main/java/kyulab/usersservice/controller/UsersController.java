package kyulab.usersservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kyulab.usersservice.dto.res.BasicResDto;
import kyulab.usersservice.dto.res.TokenDto;
import kyulab.usersservice.dto.req.UsersSignUpReqDto;
import kyulab.usersservice.dto.res.UsersInfoResDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.service.TokenService;
import kyulab.usersservice.service.UsersService;
import kyulab.usersservice.dto.req.UsersLoginReqDto;
import kyulab.usersservice.dto.req.UsersUpdateReqDto;
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

	@GetMapping("/{id}")
	public ResponseEntity<UsersInfoResDto> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(usersService.getUser(id));
	}

	@GetMapping("/mail/{email}/check")
	public ResponseEntity<BasicResDto> checkEmail(
			@PathVariable("email")
			@NotBlank(message = "이메일은 필수입니다.")
		 	@Email(
			 	regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
			 	message = "유효한 이메일 형식이어야 합니다."
			)
		 	String mail) {

		if (usersService.existsByEmail(mail)) {
			return ResponseEntity.ok(new BasicResDto(false, "이미 사용중인 이메일입니다."));
		}
		return ResponseEntity.ok(new BasicResDto(true, "ok"));
	}

	@GetMapping("/name/{name}/check")
	public ResponseEntity<BasicResDto> checkName(
			@PathVariable("name")
			@NotBlank(message = "이름은 필수 입력 항목입니다.")
			@Pattern(
				regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,29}$",
				message = "사용자 이름은 영문자로 시작해야 하며, 영문, 숫자, '_', '-', 만 사용할 수 있습니다."
			)
			String name) {
		if (usersService.existsByName(name)) {
			return ResponseEntity.ok(new BasicResDto(false, "이미 사용중인 이름입니다."));
		}
		return ResponseEntity.ok(new BasicResDto(true, "ok"));
	}

	@GetMapping("/refresh")
	public ResponseEntity<TokenDto> refresh(HttpServletRequest request) {
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (Objects.isNull(accessToken) || !accessToken.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().build();
		}
		long userId = Long.parseLong(tokenService.getSubject(accessToken.substring(7)));
		Users users = usersService.refresh(userId);
		String newAccessToken = tokenService.createToken(users, true);
		return ResponseEntity.ok(new TokenDto(newAccessToken));
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
	public ResponseEntity<BasicResDto> signup(@RequestBody UsersSignUpReqDto signUpReqDTO) {
		try {
			usersService.signup(signUpReqDTO);
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				if (e.getMessage().equals("email exists")) {
					return ResponseEntity.ok(new BasicResDto(false, "이미 사용중인 메일입니다."));
				} else if (e.getMessage().equals("name exists")) {
					return ResponseEntity.ok(new BasicResDto(false, "이미 사용중인 이름입니다."));
				}
			}
			return ResponseEntity.internalServerError().body(new BasicResDto(false, "server error"));
		}
		return ResponseEntity.ok(new BasicResDto(true, "회원가입을 축하합니다!"));
	}

	@PostMapping("/logout")
	public ResponseEntity<BasicResDto> logout(HttpServletRequest request) {
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (Objects.isNull(accessToken) || !accessToken.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body(new BasicResDto(false, "token not includ"));
		}
		redisTemplate.delete("refresh-" + tokenService.getSubject(accessToken.substring(7)));

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
				.body(new BasicResDto(true, "ok"));
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<UsersInfoResDto> update(@PathVariable Long id, @RequestBody UsersUpdateReqDto updateReqDTO) {
		return ResponseEntity.ok(usersService.update(id, updateReqDTO));
	}

}
