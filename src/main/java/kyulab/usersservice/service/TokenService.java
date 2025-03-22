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

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

	@Value("${jwt.access-expiredTime:360}")
	private long accessExpired;

	@Value("${jwt.refresh-expiredTime:36000}")
	private long refreshExpired;

	private final SecretUtil secretUtil;

	public String createToken(Users users, boolean isAccess) {
		Map<String, Object> jwtInfo = new HashMap<>();

		String algo = isAccess ? SignatureAlgorithm.HS256.getValue() : SignatureAlgorithm.HS512.getValue();
		jwtInfo.put(JwsHeader.ALGORITHM, algo);
		jwtInfo.put(JwsHeader.TYPE, JwsHeader.JWT_TYPE);

		Claims claims = Jwts.claims().setSubject(String.valueOf(users.getId()));
		claims.put("userName", users.getName());

		LocalDateTime localDateTime = LocalDateTime.now();
		Date issuedAt = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		long time = isAccess ? accessExpired : refreshExpired;
		Date expiration = Date.from(localDateTime.plusSeconds(time).atZone(ZoneId.systemDefault()).toInstant());

		SecretKey sign = isAccess ? secretUtil.getAccessKey() : secretUtil.getRefreshKey();
		return Jwts.builder()
				.setHeader(jwtInfo)
				.setClaims(claims)
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.signWith(sign)
				.compact();
	}

}
