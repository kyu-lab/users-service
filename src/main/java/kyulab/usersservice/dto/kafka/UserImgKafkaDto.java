package kyulab.usersservice.dto.kafka;

import kyulab.usersservice.domain.UserImgType;

/**
 * 카프카를 통해 전달받은 사용자 이미지 url
 * @param userId 사용자 아이디
 * @param imgUrl 이미지 url
 * @param type	 이미지 타입
 */
public record UserImgKafkaDto(long userId, String imgUrl, UserImgType type) {
}
