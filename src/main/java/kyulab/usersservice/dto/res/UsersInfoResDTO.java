package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

public record UsersInfoResDTO(Long id, String name) {

	public static UsersInfoResDTO from(Users users) {
		return new UsersInfoResDTO(
				users.getId(),
				users.getName()
		);
	}

}
