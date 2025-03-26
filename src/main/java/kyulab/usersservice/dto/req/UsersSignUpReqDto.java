package kyulab.usersservice.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsersSignUpReqDto(
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(
		regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
		message = "유효한 이메일 형식이어야 합니다."
	)
	String email,
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	@Pattern(
		regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,29}$",
		message = "사용자 이름은 영문자로 시작해야 하며, 영문, 숫자, '_', '-', 만 사용할 수 있습니다."
	)
	String name,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(
		min = 8,
		max = 100,
		message = "비밀번호는 8 ~ 100 자 사이여야 합니다."
	)
	String password,
	String imgUrl) {
}
