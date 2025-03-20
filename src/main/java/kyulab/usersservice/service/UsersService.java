package kyulab.usersservice.service;

import kyulab.usersservice.dto.res.UsersInfoResDTO;
import kyulab.usersservice.dto.req.UsersLoginReqDTO;
import kyulab.usersservice.dto.req.UsersSignUpReqDTO;
import kyulab.usersservice.dto.req.UsersUpdateReqDTO;
import kyulab.usersservice.dto.res.UsersLoginResDTO;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.BadRequestException;
import kyulab.usersservice.handler.exception.UserNotFoundException;
import kyulab.usersservice.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;
	private final StringRedisTemplate redisTemplate;

	@Value("${jwt.refresh-expiredTime:36000}")
	private Long refreshExpiredTime;

	@Transactional(readOnly = true)
	@Cacheable("user")
	public UsersInfoResDTO getUser(Long id) {
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("Inavlid User");
				});
		return UsersInfoResDTO.from(users);
	}

	@Transactional
	public UsersLoginResDTO login(UsersLoginReqDTO loginReqDTO) {
		Users users = usersRepository.findByName(loginReqDTO.name())
				.orElseThrow(() -> {
					log.info("Fail UserName : {}", loginReqDTO.name());
					return new UserNotFoundException("User Not Found");
				});

		if (!passwordEncoder.matches(loginReqDTO.passWord(), users.getPassWord())) {
			throw new UserNotFoundException("Wrong Password");
		}

		redisTemplate.opsForValue().set("refresh-" + users.getId(), tokenService.createToken(users, false), refreshExpiredTime, TimeUnit.SECONDS);
		return UsersLoginResDTO.from(users, tokenService.createToken(users, true));
	}

	@Transactional
	public void signUp(UsersSignUpReqDTO signUpDTO) {
		if (usersRepository.existsByName(signUpDTO.name())) {
			throw new BadRequestException("사용중인 계정입니다.");
		}
		Users users = new Users(signUpDTO.name(), passwordEncoder.encode(signUpDTO.passWord()));
		usersRepository.save(users);
	}

	public void logout(Long id) {
		redisTemplate.delete("refresh-" + id);
	}

	@Transactional
	public UsersLoginResDTO refresh(Long id) {
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Id Not Found : {}", id);
					return new UserNotFoundException("User Not Found");
				});
		return UsersLoginResDTO.from(users, tokenService.createToken(users, true));
	}

	@Transactional
	@CacheEvict(value = "user", key = "#id")
	public UsersInfoResDTO update(Long id, UsersUpdateReqDTO updateReqDTO) {
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("User Not Found");
				});

		if (!StringUtils.hasText(updateReqDTO.passWord())) {
			throw new BadRequestException("비밀번호가 누락되었습니다.");
		}

		users.setPassWord(passwordEncoder.encode(updateReqDTO.passWord()));
		return UsersInfoResDTO.from(users);
	}

}
