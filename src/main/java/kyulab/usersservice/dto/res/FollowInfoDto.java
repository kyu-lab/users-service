package kyulab.usersservice.dto.res;

/**
 * 팔로우 사용자의 정보를 반환합니다.
 * @param userId	대상 아이디
 * @param name		이름
 * @param iconUrl	아이콘 url
 * @param bannerUrl	배너 url
 */
public record FollowInfoDto(long userId, String name, String iconUrl, String bannerUrl, String description) {
	public FollowInfoDto(long userId, String name, String iconUrl, String bannerUrl, String description) {
		this.userId = userId;
		this.name = name;
		this.iconUrl = iconUrl;
		this.bannerUrl = bannerUrl;
		this.description = description;
	}
}
