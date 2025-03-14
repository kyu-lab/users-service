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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	@Transactional(readOnly = true)
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

		return UsersLoginResDTO.from(users, tokenService.createToken(users));
	}

	@Transactional
	public void signUp(UsersSignUpReqDTO signUpDTO) {
		if (usersRepository.existsByName(signUpDTO.name())) {
			throw new BadRequestException("사용중인 계정입니다.");
		}
		Users users = new Users(signUpDTO.name(), passwordEncoder.encode(signUpDTO.passWord()));
		usersRepository.save(users);
	}

	@Transactional
	public UsersInfoResDTO update(Long id, UsersUpdateReqDTO updateReqDTO) {
		Users users = usersRepository.findById(id)
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", id);
					return new UserNotFoundException("User Not Found");
				});
		users.setPassWord(passwordEncoder.encode(updateReqDTO.passWord()));
		return UsersInfoResDTO.from(users);
	}

}
