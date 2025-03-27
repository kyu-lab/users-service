package kyulab.usersservice.dto.gateway;

import kyulab.usersservice.dto.res.UsersInfoResDto;

import java.util.List;

public record UsersList(
		List<UsersInfoResDto> userList,
		List<Long> failList
) {
}
