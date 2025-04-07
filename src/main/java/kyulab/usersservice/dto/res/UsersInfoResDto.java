package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

public record UsersInfoResDto(String email, String name) {

	public static UsersInfoResDto from(Users users) {
		return new UsersInfoResDto(
				users.getEmail(),
				users.getName()
		);
	}

}
