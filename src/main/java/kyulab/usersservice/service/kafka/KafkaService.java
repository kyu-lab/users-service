package kyulab.usersservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kyulab.usersservice.dto.kafka.UserImgKafkaDto;
import kyulab.usersservice.handler.exception.ServerErrorExcpetion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

	private final UsersKafkaService usersKafkaService;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void sendMsg(String topic, Object msg) {
		try {
			kafkaTemplate.send(topic, msg);
		} catch (Exception e) {
			log.error("카프카 전송 실패... topic={}, msg={}", topic, msg);
			log.error("에러 메시지 : {}", e.getMessage());
			throw new ServerErrorExcpetion("데이터 전송 실패");
		}
	}

	@KafkaListener(topics = "user-image-url", groupId = "users-group")
	public void consumeUserImg(ConsumerRecord<String, String> record) {
		try {
			UserImgKafkaDto userImgKafkaDto = objectMapper.readValue(record.value(), UserImgKafkaDto.class);
			usersKafkaService.updateUserImg(userImgKafkaDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
