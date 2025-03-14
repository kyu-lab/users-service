package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

public record UsersLoginResDTO(UsersInfoResDTO userInfo, String token) {

	public static UsersLoginResDTO from(Users users, String token) {
		return new UsersLoginResDTO(
				UsersInfoResDTO.from(users),
				token
		);
	}

}
