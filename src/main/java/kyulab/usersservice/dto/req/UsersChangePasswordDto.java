package kyulab.usersservice.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * 로그인창에서 비밀번호 변경시 사용됩니다.
 * @param email		이메일
 * @param password	비밀번호
 */
public record UsersChangePasswordDto(
	@Email(
		regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
		message = "유효한 이메일 형식이어야 합니다."
	)
	String email,
	@Size(
		min = 8,
		max = 100,
		message = "비밀번호는 8 ~ 100 자 사이여야 합니다."
	)
	String password) {
}
