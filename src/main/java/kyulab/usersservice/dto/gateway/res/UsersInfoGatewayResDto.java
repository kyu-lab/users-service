package kyulab.usersservice.dto.gateway.res;

/**
 * 요청 사용자 정보
 * @param id 		아이디(pk)
 * @param email 	이메일
 * @param name		이름
 * @param iconUrl	사용자 아이콘 이미지 url
 * @param bannerUrl	사용자 배너 이미지 url
 * @param isFollow	나와의 팔로우 관계
 */
public record UsersInfoGatewayResDto(Long id, String email, String name, String iconUrl, String bannerUrl, boolean isFollow) {
	public UsersInfoGatewayResDto(Long id, String email, String name, String iconUrl, String bannerUrl, boolean isFollow) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.iconUrl = iconUrl;
		this.bannerUrl = bannerUrl;
		this.isFollow = isFollow;
	}
}
