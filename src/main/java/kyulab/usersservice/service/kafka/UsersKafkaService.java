package kyulab.usersservice.service.kafka;

import kyulab.usersservice.dto.kafka.UserImgDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.UserNotFoundException;
import kyulab.usersservice.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersKafkaService {

	private final UsersRepository usersRepository;

	@Transactional
	public void updateUserImg(UserImgDto userImgDto) {
		Users users = usersRepository.findById(userImgDto.userId())
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", userImgDto.userId());
					return new UserNotFoundException("Inavlid User");
				});
		users.updateUserImg(userImgDto.userImgPath());
	}

}
