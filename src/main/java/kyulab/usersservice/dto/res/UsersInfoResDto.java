package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

public record UsersInfoResDto(long id, String email, String name, String imgUrl) {

	public static UsersInfoResDto from(Users users) {
		return new UsersInfoResDto(
				users.getId(),
				users.getEmail(),
				users.getName(),
				users.getImgUrl()
		);
	}

}
