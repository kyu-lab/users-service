package kyulab.usersservice.dto.kafka;

import kyulab.usersservice.entity.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsersDto {
	private long id;
	private String name;

	public UsersDto(Users users) {
		this.id = users.getId();
		this.name = users.getName();
	}
}
