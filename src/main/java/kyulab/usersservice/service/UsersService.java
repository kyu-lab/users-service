package kyulab.usersservice.service;

import kyulab.usersservice.dto.gateway.UsersGroupCreateDto;
import kyulab.usersservice.dto.req.UsersChangePasswordReqDto;
import kyulab.usersservice.dto.res.UsersInfoResDto;
import kyulab.usersservice.dto.req.UsersLoginReqDto;
import kyulab.usersservice.dto.req.UsersSignUpReqDto;
import kyulab.usersservice.dto.req.UsersUpdateReqDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.BadRequestException;
import kyulab.usersservice.handler.exception.ServiceUnabailabeExcpetion;
import kyulab.usersservice.handler.exception.UserNotFoundException;
import kyulab.usersservice.repository.UsersRepository;
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
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	@Cacheable("user")
	public UsersInfoResDto getUser(Long id) {
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("Inavlid User");
				});
		return UsersInfoResDto.from(users);
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

		return users;
	}

	@Transactional
	public void signup(UsersSignUpReqDto signUpDTO) {
		if (usersRepository.existsByEmail(signUpDTO.email())) {
			throw new IllegalArgumentException("email exists");
		}
		if (usersRepository.existsByName(signUpDTO.name())) {
			throw new IllegalArgumentException("name exists");
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
			throw new ServiceUnabailabeExcpetion("post-service unabliable");
		}
	}

	@Transactional(readOnly = true)
	public Users refresh(Long id) {
		return usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Id Not Found : {}", id);
					return new UserNotFoundException("User Not Found");
				});
	}

	@Transactional
	public void changePassword(UsersChangePasswordReqDto passwordReqDto) {
		Users users = usersRepository.findByEmail(passwordReqDto.email())
				.orElseThrow(() -> {
					log.info("Cant find email : {}", passwordReqDto.email());
					return new UserNotFoundException("User Not Found");
				});
		users.setPassword(passwordEncoder.encode(passwordReqDto.password()));
	}

	@Transactional
	@CacheEvict(value = "user", key = "T(kyulab.usersservice.util.UserContext).getUserId()")
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

		users.setPassword(passwordEncoder.encode(updateReqDTO.passWord()));
		return UsersInfoResDto.from(users);
	}

}
