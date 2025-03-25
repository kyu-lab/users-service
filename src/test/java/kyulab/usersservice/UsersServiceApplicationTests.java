package kyulab.usersservice;

import kyulab.usersservice.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UsersServiceApplicationTests {

	@Autowired
	private UsersService usersService;

	/*@BeforeEach
	@DisplayName("테스트전 계정 생성")
	void setUp() {
		UsersSignUpReqDto signUpDTO = new UsersSignUpReqDto("테스트1", "pwd");
		usersService.signUp(signUpDTO);
	}

	@Test
	@DisplayName("사용자 조회")
	void getUser() {
		UsersLoginReqDto loginDTO = new UsersLoginReqDto("테스트1", "pwd");
		UsersLoginResDto loginRes = usersService.login(loginDTO);
		assertNotNull(usersService.getUser(loginRes.userInfo().id()));
		assertNotNull(loginRes.token());
	}

	@Test
	@DisplayName("사용자 업데이트")
	void update() {
		UsersLoginReqDto loginDTO = new UsersLoginReqDto("테스트1", "pwd");
		UsersLoginResDto loginRes = usersService.login(loginDTO);
		UsersUpdateReqDto updateDTO = new UsersUpdateReqDto("ppp");
		assertNotNull(usersService.update(loginRes.userInfo().id(), updateDTO));
	}*/

}
