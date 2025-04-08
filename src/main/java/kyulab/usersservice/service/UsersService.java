package kyulab.usersservice.service;

import kyulab.usersservice.dto.gateway.UsersGroupCreateDto;
import kyulab.usersservice.dto.kafka.UsersDto;
import kyulab.usersservice.dto.req.UsersChangePasswordReqDto;
import kyulab.usersservice.dto.res.UsersInfoResDto;
import kyulab.usersservice.dto.req.UsersLoginReqDto;
import kyulab.usersservice.dto.req.UsersSignUpReqDto;
import kyulab.usersservice.dto.req.UsersUpdateReqDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.BadRequestException;
import kyulab.usersservice.handler.exception.ConflictRequestException;
import kyulab.usersservice.handler.exception.ServiceUnavailabeExcpetion;
import kyulab.usersservice.handler.exception.UserNotFoundException;
import kyulab.usersservice.repository.UsersRepository;
import kyulab.usersservice.service.gateway.GroupGatewayService;
import kyulab.usersservice.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

	private final GroupGatewayService groupGatewayService;
	private final KafkaService kafkaService;
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public Users getUser(Long id) {
		return usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("Inavlid User");
				});

	}

	@Transactional(readOnly = true)
	@Cacheable("userInfo")
	public UsersInfoResDto getUserInfo() {
		long userId = UserContext.getUserId();
		return UsersInfoResDto.from(getUser(userId));
	}

	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return usersRepository.existsByEmail(email);
	}

	@Transactional(readOnly = true)
	public boolean existsByName(String name) {
		return usersRepository.existsByName(name);
	}

	@Transactional(readOnly = true)
	public Users login(UsersLoginReqDto loginReqDTO) {
		Users users = usersRepository.findByEmail(loginReqDTO.email())
				.orElseThrow(() -> {
					log.info("Cant find email : {}", loginReqDTO.email());
					return new UserNotFoundException("User Not Found");
				});

		if (!passwordEncoder.matches(loginReqDTO.password(), users.getPassword())) {
			throw new UserNotFoundException("Wrong Password");
		}

		if (users.getIsDelete().equals(Boolean.TRUE)) {
			throw new UserNotFoundException("Delete User");
		}

		return users;
	}

	@Transactional
	public void signup(UsersSignUpReqDto signUpDTO) {
		if (usersRepository.existsByEmail(signUpDTO.email())) {
			throw new ConflictRequestException("email Already exists");
		}

		if (usersRepository.existsByName(signUpDTO.name())) {
			throw new ConflictRequestException("name Already exists");
		}

		Users users = new Users(
			signUpDTO.email(),
			signUpDTO.name(),
			passwordEncoder.encode(signUpDTO.password())
		);
		usersRepository.save(users);

		// post-service에게 사용자 그룹 생성을 요청한다.
		UsersGroupCreateDto createDto = new UsersGroupCreateDto(
				users.getId(), users.getName(), users.getImgUrl()
		);

		if (!groupGatewayService.reqeusetPostUserGroup(createDto)) {
			throw new ServiceUnavailabeExcpetion("post-service unabliable");
		}

		// 사용자 검색을 위해 요청을 보낸다.
		// 그룹 검색을 위해 추가
		UsersDto usersDto = new UsersDto(users);
		kafkaService.sendMsg("users-search", usersDto);
	}

	@Transactional
	public void changePassword(UsersChangePasswordReqDto passwordReqDto) {
		Users users = usersRepository.findByEmail(passwordReqDto.email())
				.orElseThrow(() -> {
					log.info("Cant find email : {}", passwordReqDto.email());
					return new UserNotFoundException("User Not Found");
				});
		users.updatePassword(passwordEncoder.encode(passwordReqDto.password()));
	}

	@Transactional
	@CacheEvict(value = "userInfo", key = "T(kyulab.usersservice.util.UserContext).getUserId()")
	public UsersInfoResDto update(UsersUpdateReqDto updateReqDTO) {
		long id = UserContext.getUserId();
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("User Not Found");
				});

		if (!StringUtils.hasText(updateReqDTO.passWord())) {
			throw new BadRequestException("비밀번호가 누락되었습니다.");
		}

		users.updatePassword(passwordEncoder.encode(updateReqDTO.passWord()));
		return UsersInfoResDto.from(users);
	}

	@Transactional
	public void delete() {
		long id = UserContext.getUserId();
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("User Not Found");
				});

		if (users.getIsDelete().equals(Boolean.TRUE)) {
			throw new BadRequestException("Already delete User");
		}
		users.deleteUsers();
	}

}
