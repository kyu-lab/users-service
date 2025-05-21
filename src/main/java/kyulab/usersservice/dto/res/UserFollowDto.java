package kyulab.usersservice.dto.res;

import java.util.List;

public record UserFollowDto(List<FollowInfoDto> userList, Long nextCursor, boolean hasMore) {
	public static UserFollowDto from(List<FollowInfoDto> userList, int limit) {
		return new UserFollowDto(
				userList,
				userList.isEmpty() ? null : userList.get(userList.size() - 1).userId(),
				userList.size() > limit
		);
	}
}
