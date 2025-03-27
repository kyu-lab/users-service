package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

public record UsersInfoResDto(Long id, String name) {

	public static UsersInfoResDto from(Users users) {
		return new UsersInfoResDto(
				users.getId(),
				users.getName()
		);
	}

}
