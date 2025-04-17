package kyulab.usersservice;

import kyulab.usersservice.dto.kafka.UsersKafkaDto;
import kyulab.usersservice.dto.req.UsersLoginDto;
import kyulab.usersservice.dto.req.UsersSignUpDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.repository.UsersRepository;
import kyulab.usersservice.service.TokenService;
import kyulab.usersservice.service.UsersService;
import kyulab.usersservice.service.kafka.KafkaService;
import kyulab.usersservice.util.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 테스트 코드 작성 페이지로 작동에 실패하면 심각한 메서드를 테스트 해주세요.
 */
@SpringBootTest
class UsersServiceApplicationTests {

	@Autowired
	private UsersService usersService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsersRepository usersRepository;

	@MockitoBean
	private KafkaService kafkaService;

	@BeforeEach
	void setUp() {
		UsersSignUpDto dto = new UsersSignUpDto("test@email.com", "tester", "password");
		usersService.signUp(dto);

		UsersLoginDto loginDTO = new UsersLoginDto("test@email.com", "password");
		long userId = usersService.login(loginDTO).getId();
		UserContext.setUserId(userId); // 테스트용

		reset(kafkaService); // 중복 호출로 에러 방지
	}
	
	@Test
	@DisplayName("계정 생성")
	@Transactional
	void signUp() {
		// given
		UsersSignUpDto dto = new UsersSignUpDto("signup@email.com", "signup", "password");

		// when
		usersService.signUp(dto);

		// then
		Users saved = usersRepository.findByEmail("signup@email.com").orElseThrow();
		assertNotNull(saved.getId());
		assertEquals("signup", saved.getName());

		// 카프카 실행 확인
		verify(kafkaService, atLeastOnce()).sendMsg(eq("users-search"), any(UsersKafkaDto.class));
	}

	@Test
	@DisplayName("로그인")
	@Transactional
	void login() {
		// given
		UsersLoginDto loginDTO = new UsersLoginDto("test@email.com", "password");

		// when
		Users users = usersService.login(loginDTO);

		// then
		assertNotNull(tokenService.createToken(users, false));
		assertNotNull(tokenService.createToken(users, true));
	}

}
