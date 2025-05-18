package kyulab.usersservice.dto.res;

import kyulab.usersservice.entity.Users;

/**
 * 사용자 정보
 * @param id				아이디
 * @param email				이메일
 * @param name				사용자 이름
 * @param iconUrl			아이콘 url
 * @param bannerUrl			배너 url
 * @param postCount			게시물 작성 갯수
 * @param followerCount		팔로워수
 * @param followingCount	팔로잉수
 */
public record UsersInfoDto(long id, String email, String name, String iconUrl, String bannerUrl, long postCount, long followerCount, long followingCount) {
	public static UsersInfoDto from(Users users, long postCount, FollowingCountDto followingCountDto) {
		return new UsersInfoDto(
				users.getId(),
				users.getEmail(),
				users.getName(),
				users.getIconUrl(),
				users.getBannerUrl(),
				postCount,
				followingCountDto.followCount(),
				followingCountDto.followingCount()
		);
	}
}
