package kyulab.usersservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kyulab.usersservice.util.SecretUtil;
import kyulab.usersservice.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

	@Value("${jwt.expiredTime}")
	private long accessExpired;

	private final SecretUtil secretUtil;

	public String createToken(Users users) {
		Map<String, Object> jwtInfo = new HashMap<>();
		jwtInfo.put(JwsHeader.ALGORITHM, SignatureAlgorithm.HS512.getValue());
		jwtInfo.put(JwsHeader.TYPE, JwsHeader.JWT_TYPE);

		Claims claims = Jwts.claims().setSubject(String.valueOf(users.getId()));
		claims.put("userName", users.getName());

		LocalDateTime localDateTime = LocalDateTime.now();
		Date issuedAt = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date expiration = Date.from(localDateTime.plusSeconds(accessExpired).atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
				.setHeader(jwtInfo)
				.setClaims(claims)
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.signWith(secretUtil.getSecretKey())
				.compact();
	}

}
