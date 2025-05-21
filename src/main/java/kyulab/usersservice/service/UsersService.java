package kyulab.usersservice.service;

import kyulab.usersservice.domain.UserStatus;
import kyulab.usersservice.dto.kafka.UsersKafkaDto;
import kyulab.usersservice.dto.req.UsersChangePasswordDto;
import kyulab.usersservice.dto.req.UsersLoginDto;
import kyulab.usersservice.dto.res.FollowingCountDto;
import kyulab.usersservice.dto.res.UsersInfoDto;
import kyulab.usersservice.dto.req.UsersSignUpDto;
import kyulab.usersservice.dto.req.UsersUpdateReqDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.BadRequestException;
import kyulab.usersservice.handler.exception.ConflictRequestException;
import kyulab.usersservice.handler.exception.NotFoundException;
import kyulab.usersservice.repository.FollowRepository;
import kyulab.usersservice.repository.UsersRepository;
import kyulab.usersservice.service.gateway.GatewayService;
import kyulab.usersservice.service.kafka.KafkaService;
import kyulab.usersservice.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

	private final KafkaService kafkaService;
	private final GatewayService gatewayService;
	private final UsersRepository usersRepository;
	private final FollowRepository followRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public Users getUser(Long id) {
		return usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new NotFoundException("Inavlid User");
				});
	}

	@Transactional(readOnly = true)
	public UsersInfoDto getUserInfo(long userId) {
		Users users = getUser(userId);
		long postCount = gatewayService.requestPostCount(userId);
		FollowingCountDto followingCountDto = followRepository.countFollowingByUserId(userId);
		return UsersInfoDto.from(users, postCount, followingCountDto);
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
	public Users login(UsersLoginDto loginReqDTO) {
		Users users = usersRepository.findByEmail(loginReqDTO.email())
				.orElseThrow(() -> {
					log.info("Cant find email : {}", loginReqDTO.email());
					return new NotFoundException("User Not Found");
				});

		if (!passwordEncoder.matches(loginReqDTO.password(), users.getPassword())) {
			throw new NotFoundException("Wrong Password");
		}

		if (users.getStatus() == UserStatus.DELETE) {
			throw new NotFoundException("Delete User");
		}

		if (users.getStatus() == UserStatus.BAN) {
			throw new BadRequestException("BAN User");
		}

		return users;
	}

	@Transactional
	public void signUp(UsersSignUpDto signUpDTO) {
		if (usersRepository.existsByEmail(signUpDTO.email())) {
			throw new ConflictRequestException("email Already exists");
		}

		if (usersRepository.existsByName(signUpDTO.name())) {
			throw new ConflictRequestException("name Already exists");
		}

		// 사용자 생성
		Users users = usersRepository.save(new Users(
				signUpDTO.email(), signUpDTO.name(), passwordEncoder.encode(signUpDTO.password())
		));

		// 사용자 검색을 위해 검색 애플리케이션으로 데이터 전송
		UsersKafkaDto usersKafkaDto = UsersKafkaDto.from(users);
		kafkaService.sendMsg("users-search", usersKafkaDto);
	}

	@Transactional
	public void changePassword(UsersChangePasswordDto passwordReqDto) {
		Users users = usersRepository.findByEmail(passwordReqDto.email())
				.orElseThrow(() -> {
					log.info("Cant find email : {}", passwordReqDto.email());
					return new NotFoundException("User Not Found");
				});
		users.updatePassword(passwordEncoder.encode(passwordReqDto.password()));
	}

	@Transactional
	public void updateUser(UsersUpdateReqDto updateReqDTO) {
		long id = UserContext.getUserId();
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new NotFoundException("User Not Found");
				});

		if (!StringUtils.hasText(updateReqDTO.passWord())) {
			throw new BadRequestException("비밀번호가 누락되었습니다.");
		}

		users.updatePassword(passwordEncoder.encode(updateReqDTO.passWord()));
	}

	@Transactional
	public void delete() {
		long id = UserContext.getUserId();
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new NotFoundException("User Not Found");
				});

		if (users.getStatus() == UserStatus.DELETE) {
			throw new BadRequestException("Already delete");
		}

		users.updateUserStatus(UserStatus.DELETE);
		UsersKafkaDto usersKafkaDto = UsersKafkaDto.from(users);
		kafkaService.sendMsg("users-search-delete", usersKafkaDto);
	}

}
