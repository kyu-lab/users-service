package kyulab.usersservice.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 로그인시 사용됩니다.
 * @param email		이메일
 * @param password	비밀번호
 */
public record UsersLoginDto(
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(
		regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
		message = "유효한 이메일 형식이어야 합니다."
	)
	String email,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(
		min = 8,
		max = 100,
		message = "비밀번호는 8 ~ 100 자 사이여야 합니다."
	)
	String password
) {
}
