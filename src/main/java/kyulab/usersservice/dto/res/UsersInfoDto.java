package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

/**
 * 사용자 정보
 * @param id		아이디
 * @param email		이메일
 * @param name		사용자 이름
 * @param iconUrl	아이콘 url
 * @param bannerUrl	배너 url
 */
public record UsersInfoDto(long id, String email, String name, String iconUrl, String bannerUrl) {
	public static UsersInfoDto from(Users users) {
		return new UsersInfoDto(
				users.getId(),
				users.getEmail(),
				users.getName(),
				users.getIconUrl(),
				users.getBannerUrl()
		);
	}
}
