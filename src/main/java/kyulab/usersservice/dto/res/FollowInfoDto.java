package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

/**
 * 팔로우 사용자의 정보를 반환합니다.
 * @param userId	대상 아이디
 * @param name		이름
 * @param iconUrl	아이콘 url
 * @param bannerUrl	배너 url
 */
public record FollowInfoDto(long userId, String name, String iconUrl, String bannerUrl) {
	public static FollowInfoDto from(Users users) {
		return new FollowInfoDto(
				users.getId(),
				users.getName(),
				users.getIconUrl(),
				users.getBannerUrl()
		);
	}
}
