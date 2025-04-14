package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

public record FollowDto(
		Long userId,
		String name,
		String imgUrl) {
	public static FollowDto from(Users users) {
		return new FollowDto(
				users.getId(),
				users.getName(),
				users.getImgUrl()
		);
	}
}
