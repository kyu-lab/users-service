package kyulab.usersservice;

import kyulab.usersservice.dto.req.UsersLoginReqDTO;
import kyulab.usersservice.dto.req.UsersSignUpReqDTO;
import kyulab.usersservice.dto.req.UsersUpdateReqDTO;
import kyulab.usersservice.dto.res.UsersLoginResDTO;
import kyulab.usersservice.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UsersServiceApplicationTests {

	@Autowired
	private UsersService usersService;

	@BeforeEach
	@DisplayName("테스트전 계정 생성")
	void setUp() {
		UsersSignUpReqDTO signUpDTO = new UsersSignUpReqDTO("테스트1", "pwd");
		usersService.signUp(signUpDTO);
	}

	@Test
	@DisplayName("사용자 조회")
	void getUser() {
		UsersLoginReqDTO loginDTO = new UsersLoginReqDTO("테스트1", "pwd");
		UsersLoginResDTO loginRes = usersService.login(loginDTO);
		assertNotNull(usersService.getUser(loginRes.userInfo().id()));
		assertNotNull(loginRes.token());
	}

	@Test
	@DisplayName("사용자 업데이트")
	void update() {
		UsersLoginReqDTO loginDTO = new UsersLoginReqDTO("테스트1", "pwd");
		UsersLoginResDTO loginRes = usersService.login(loginDTO);
		UsersUpdateReqDTO updateDTO = new UsersUpdateReqDTO("ppp");
		assertNotNull(usersService.update(loginRes.userInfo().id(), updateDTO));
	}
}
