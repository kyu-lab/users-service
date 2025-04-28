package kyulab.usersservice.service.kafka;

import kyulab.usersservice.domain.UserImgType;
import kyulab.usersservice.dto.kafka.UserImgKafkaDto;
import kyulab.usersservice.entity.Users;
import kyulab.usersservice.handler.exception.NotFoundException;
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
	public void updateUserImg(UserImgKafkaDto userImgKafkaDto) {
		Users users = usersRepository.findById(userImgKafkaDto.userId())
				.orElseThrow(() -> {
					log.info("Fail UserId : {}", userImgKafkaDto.userId());
					return new NotFoundException("Inavlid User");
				});
		if (userImgKafkaDto.type() == UserImgType.ICON) {
			users.updateIconUrl(userImgKafkaDto.imgUrl());
		} else if (userImgKafkaDto.type() == UserImgType.BANNER) {
			users.updateBannerUrl(userImgKafkaDto.imgUrl());
		}
	}

}
