package kyulab.usersservice.dto.res;

/**
 * 사용자의 팔로워와 팔로잉 카운트
 * @param followCount    	팔로워 카운트
 * @param followingCount	팔로잉 카운트
 */
public record FollowingCountDto(long followCount, long followingCount) {
	public FollowingCountDto(long followCount, long followingCount) {
		this.followCount = followCount;
		this.followingCount = followingCount;
	}
}
