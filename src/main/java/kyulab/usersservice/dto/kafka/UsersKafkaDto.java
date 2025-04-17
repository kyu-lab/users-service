package kyulab.usersservice.dto.kafka;

import kyulab.usersservice.entity.Users;

public record UsersKafkaDto(long id, String name) {
	public static UsersKafkaDto from(Users users) {
		return new UsersKafkaDto(users.getId(), users.getName());
	}
}
