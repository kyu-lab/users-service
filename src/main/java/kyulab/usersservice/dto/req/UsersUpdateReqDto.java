package kyulab.usersservice.dto.req;

/**
 * 사용자 정보 업데이트시 사용됩니다.
 * @param passWord	비밀번호
 */
public record UsersUpdateReqDto(String passWord) {
}
